package net.squanchy.settings

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import arrow.core.Option
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import net.squanchy.BuildConfig
import net.squanchy.R
import net.squanchy.analytics.Analytics
import net.squanchy.navigation.Navigator
import net.squanchy.remoteconfig.RemoteConfig
import net.squanchy.remoteconfig.WifiConfiguration
import net.squanchy.remoteconfig.wifiAutoConfigEnabledNow
import net.squanchy.service.repository.User
import net.squanchy.signin.SignInOrigin
import net.squanchy.signin.SignInService
import net.squanchy.support.lang.getOrThrow
import net.squanchy.wificonfig.WifiConfigOrigin
import net.squanchy.wificonfig.WifiConfigService
import timber.log.Timber

class SettingsFragment : PreferenceFragment() {

    private val subscriptions = CompositeDisposable()

    private lateinit var signInService: SignInService
    private lateinit var navigator: Navigator
    private lateinit var analytics: Analytics
    private lateinit var remoteConfig: RemoteConfig
    private lateinit var wifiConfigService: WifiConfigService

    private lateinit var accountCategory: PreferenceCategory
    private lateinit var accountEmailPreference: Preference
    private lateinit var accountSignInSignOutPreference: Preference

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
            wifiConfigService = wifiConfigService()
        }

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

        val favoritesInSchedulePreference = findPreference(getString(R.string.favorites_in_schedule_preference_key))
        favoritesInSchedulePreference.setOnPreferenceChangeListener { _, enabled ->
            if (enabled as Boolean) {
                analytics.trackFavoritesInScheduleEnabled()
            } else {
                analytics.trackFavoritesInScheduleDisabled()
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

        if (wifiAutoConfigEnabledNow(remoteConfig)) {
            wifiPreference.setOnPreferenceClickListener { setupWifi(); true }
        } else {
            val settingsCategory = findPreference(getString(R.string.settings_category_key)) as PreferenceCategory
            settingsCategory.removePreference(wifiPreference)
        }
    }

    private fun setupWifi() {
        wifiConfigService.setupWifi(WifiConfigOrigin.SETTINGS, callback = object : WifiConfigService.Callback {
            override fun onSuccess(wifiConfiguration: WifiConfiguration) {
                Snackbar.make(viewOrThrow, R.string.settings_message_wifi_success, Snackbar.LENGTH_SHORT).show()
            }

            override fun onFailure(wifiConfiguration: WifiConfiguration) {
                navigator.toWifiConfigError(wifiConfiguration)
            }
        })
    }

    override fun onStart() {
        super.onStart()

        hideDividers()

        subscriptions.add(
            signInService.currentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onUserChanged, Timber::e)
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
            subscriptions.add(
                signInService.signOut()
                    .subscribe(
                        {
                            Snackbar.make(viewOrThrow, R.string.settings_message_signed_out, Snackbar.LENGTH_SHORT).show()
                            analytics.trackUserNotLoggedIn()
                        },
                        Timber::e
                    )
            )
            return@setOnPreferenceClickListener true
        }
    }

    private val viewOrThrow: View
        get() = this.view ?: throw IllegalStateException("You cannot access the fragment's view when it doesn't exist yet")

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
