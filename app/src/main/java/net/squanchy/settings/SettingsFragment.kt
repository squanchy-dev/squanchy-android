package net.squanchy.settings

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ListView
import com.google.firebase.auth.FirebaseUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import net.squanchy.BuildConfig
import net.squanchy.R
import net.squanchy.navigation.Navigator
import net.squanchy.signin.SignInService
import net.squanchy.support.lang.Optional

class SettingsFragment : PreferenceFragment() {

    private val subscriptions = CompositeDisposable()

    private lateinit var signInService: SignInService
    private lateinit var navigator: Navigator

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

        val activity = activity as AppCompatActivity // TODO UNYOLO
        val component = settingsFragmentComponent(activity)

        signInService = component.signInService()
        navigator = component.navigator()

        accountCategory = findPreference(getString(R.string.account_category_key)) as PreferenceCategory
        accountEmailPreference = findPreference(getString(R.string.account_email_preference_key))
        accountCategory.removePreference(accountEmailPreference)
        accountSignInSignOutPreference = findPreference(getString(R.string.account_signin_signout_preference_key))

        val aboutPreference = findPreference(getString(R.string.about_preference_key))
        aboutPreference.setOnPreferenceClickListener {
            navigator.toAboutSquanchy()
            true
        }
    }

    private fun displayBuildVersion() {
        val buildVersionKey = getString(R.string.build_version_preference_key)
        val buildVersionPreference = findPreference(buildVersionKey)
        val buildVersion = String.format(getString(R.string.version_x), BuildConfig.VERSION_NAME)
        buildVersionPreference.title = buildVersion
    }

    private fun removeDebugCategory() {
        val debugCategoryKey = getString(R.string.debug_category_preference_key)
        val debugCategory = findPreference(debugCategoryKey)
        preferenceScreen.removePreference(debugCategory)
    }

    override fun onStart() {
        super.onStart()

        hideDividers()

        subscriptions.add(
                signInService.currentUser()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { onUserChanged(it) }
        )
    }

    private fun hideDividers() {
        val list = viewOrThrow.findViewById<ListView>(android.R.id.list)
        list.divider = null
        list.dividerHeight = 0
    }

    private fun onUserChanged(user: Optional<FirebaseUser>) {
        if (user.isPresent && !user.get().isAnonymous) {
            onSignedInWith(user.get())
        } else {
            onSignedOut()
        }
    }

    private fun onSignedInWith(firebaseUser: FirebaseUser) {
        accountCategory.addPreference(accountEmailPreference)
        accountEmailPreference.title = firebaseUser.email

        accountSignInSignOutPreference.setTitle(R.string.sign_out_title)
        accountSignInSignOutPreference.setOnPreferenceClickListener {
            signInService.signOut()
                .subscribe { Snackbar.make(viewOrThrow, R.string.settings_message_signed_out, Snackbar.LENGTH_SHORT).show() }
            true
        }
    }

    private fun onSignedOut() {
        accountCategory.removePreference(accountEmailPreference)

        accountSignInSignOutPreference.setTitle(R.string.sign_in_title)
        accountSignInSignOutPreference.setOnPreferenceClickListener {
            navigator.toSignIn()
            true
        }
    }

    override fun onStop() {
        super.onStop()
        subscriptions.clear()
    }
}
