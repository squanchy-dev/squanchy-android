package net.squanchy.settings

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ListView
import arrow.core.Option
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import net.squanchy.BuildConfig
import net.squanchy.R
import net.squanchy.analytics.Analytics
import net.squanchy.navigation.Navigator
import net.squanchy.remoteconfig.RemoteConfig
import net.squanchy.service.repository.User
import net.squanchy.signin.SignInOrigin
import net.squanchy.signin.SignInService
import net.squanchy.support.lang.getOrThrow

class SettingsFragment : PreferenceFragment() {

    private val subscriptions = CompositeDisposable()

    private lateinit var wifiManager: WifiManager
    private lateinit var signInService: SignInService
    private lateinit var navigator: Navigator
    private lateinit var analytics: Analytics
    private lateinit var remoteConfig: RemoteConfig

    private lateinit var accountCategory: PreferenceCategory
    private lateinit var accountEmailPreference: Preference
    private lateinit var accountSignInSignOutPreference: Preference

    private val viewOrThrow: View
        get() = this.view ?: throw IllegalStateException("You cannot access the fragment's view when it doesn't exist yet")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings_preferences)
        preferenceScreen.isOrderingAsAdded = false

        displayBuildVersion()

        if (!BuildConfig.DEBUG) {
            removeDebugCategory()
        }

        val activity = activity as AppCompatActivity
        with(settingsFragmentComponent(activity)) {
            signInService = signInService()
            navigator = navigator()
            analytics = analytics()
            remoteConfig = remoteConfig()
        }

        wifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        accountCategory = findPreference(getString(R.string.account_category_key)) as PreferenceCategory
        accountEmailPreference = findPreference(getString(R.string.account_email_preference_key))
        accountCategory.removePreference(accountEmailPreference)
        accountSignInSignOutPreference = findPreference(getString(R.string.account_signin_signout_preference_key))

        val aboutPreference = findPreference(getString(R.string.about_preference_key))
        aboutPreference.setOnPreferenceClickListener {
            navigator.toAboutSquanchy()
            true
        }

        val notificationsPreference = findPreference(getString(R.string.about_to_start_notification_preference_key))
        notificationsPreference.setOnPreferenceChangeListener { _, enabled ->
            if (enabled as Boolean) {
                analytics.trackNotificationsEnabled()
            } else {
                analytics.trackNotificationsDisabled()
            }
            true
        }

        setupWifiConfigPreference()
    }

    private fun displayBuildVersion() {
        val buildVersionKey = getString(R.string.build_version_preference_key)
        val buildVersionPreference = findPreference(buildVersionKey)
        val buildVersion = getString(R.string.version_x, BuildConfig.VERSION_NAME)
        buildVersionPreference.title = buildVersion
    }

    private fun removeDebugCategory() {
        val debugCategoryKey = getString(R.string.debug_category_preference_key)
        val debugCategory = findPreference(debugCategoryKey)
        preferenceScreen.removePreference(debugCategory)
    }

    private fun setupWifiConfigPreference() {
        val wifiPreference = findPreference(getString(R.string.auto_wifi_preference_key))

        if (remoteConfig.wifiAutoConfigEnabledNow()) {
            wifiPreference.setOnPreferenceClickListener { setupWifi(); true }
        } else {
            val settingsCategory = findPreference(getString(R.string.settings_category_key)) as PreferenceCategory
            settingsCategory.removePreference(wifiPreference)
        }
    }

    private fun setupWifi() {
        val ssid = remoteConfig.wifiSsid()
        val password = remoteConfig.wifiPassword()
        val wifiConfig = WifiConfiguration()
        wifiConfig.SSID = "\"$ssid\""
        wifiConfig.preSharedKey = "\"$password\""
        val netId = wifiManager.addNetwork(wifiConfig)
        wifiManager.disconnect()
        val networkEnabled = wifiManager.enableNetwork(netId, true)
        wifiManager.reconnect()

        if (networkEnabled) {
            Snackbar.make(viewOrThrow, R.string.settings_message_wifi_success, Snackbar.LENGTH_INDEFINITE).show()
        } else {
            navigator.toWifiConfigError(ssid, password)
        }
    }

    override fun onStart() {
        super.onStart()

        hideDividers()

        subscriptions.add(
            signInService.currentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onUserChanged)
        )
    }

    private fun hideDividers() {
        val list = viewOrThrow.findViewById<ListView>(android.R.id.list)
        list.divider = null
        list.dividerHeight = 0
    }

    private fun onUserChanged(user: Option<User>) {
        if (user.isDefined() && !user.getOrThrow().isAnonymous) {
            onSignedInWith(user.getOrThrow())
        } else {
            onSignedOut()
        }
    }

    private fun onSignedInWith(user: User) {
        accountCategory.addPreference(accountEmailPreference)
        accountEmailPreference.title = user.email

        accountSignInSignOutPreference.setTitle(R.string.sign_out_title)
        accountSignInSignOutPreference.setOnPreferenceClickListener {
            signInService.signOut()
                .subscribe {
                    Snackbar.make(viewOrThrow, R.string.settings_message_signed_out, Snackbar.LENGTH_SHORT).show()
                    analytics.trackUserNotLoggedIn()
                }
            true
        }
    }

    private fun onSignedOut() {
        accountCategory.removePreference(accountEmailPreference)

        accountSignInSignOutPreference.setTitle(R.string.sign_in_title)
        accountSignInSignOutPreference.setOnPreferenceClickListener {
            navigator.toSignIn(SignInOrigin.SETTINGS)
            true
        }
    }

    override fun onStop() {
        super.onStop()
        subscriptions.clear()
    }
}
