package net.squanchy.onboarding.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_onboarding_account.*
import net.squanchy.R
import net.squanchy.fonts.TypefaceStyleableActivity
import net.squanchy.navigation.Navigator
import net.squanchy.onboarding.Onboarding
import net.squanchy.onboarding.OnboardingPage
import net.squanchy.signin.SignInService
import java.util.concurrent.TimeUnit

class AccountOnboardingActivity : TypefaceStyleableActivity() {

    private lateinit var onboarding: Onboarding
    private lateinit var navigator: Navigator
    private lateinit var signInService: SignInService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = AccountOnboardingInjector.obtain(this)
        onboarding = component.onboarding()
        navigator = component.navigator()
        signInService = component.signInService()

        setContentView(R.layout.activity_onboarding_account)

        skip_button.setOnClickListener { _ -> markPageAsSeenAndFinish() }
        location_opt_in_button.setOnClickListener { _ -> signInToGoogle() }

        setResult(Activity.RESULT_CANCELED)
    }

    override fun onStart() {
        super.onStart()

        disableUi()
        signInService.isSignedInToGoogle
                .subscribeOn(AndroidSchedulers.mainThread())
                .timeout(SIGNIN_STATE_CHECK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .subscribe(
                        { signedIn ->
                            if (signedIn) {
                                markPageAsSeenAndFinish()
                            } else {
                                enableUi()
                            }
                        },
                        { _ -> enableUi() }
                )
    }

    private fun signInToGoogle() {
        disableUi()
        navigator.toSignInForResult(REQUEST_CODE_SIGNIN)
    }

    private fun disableUi() {
        onboarding_content_root.isEnabled = false
        onboarding_content_root.alpha = DISABLED_UI_ALPHA
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode != REQUEST_CODE_SIGNIN) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        if (resultCode == Activity.RESULT_OK) {
            markPageAsSeenAndFinish()
        } else {
            enableUi()
        }
    }

    private fun markPageAsSeenAndFinish() {
        onboarding.savePageSeen(OnboardingPage.ACCOUNT)
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun enableUi() {
        onboarding_content_root.isEnabled = true
        onboarding_content_root.alpha = ENABLED_UI_ALPHA
    }

    companion object {

        private const val REQUEST_CODE_SIGNIN = 1235
        private const val SIGNIN_STATE_CHECK_TIMEOUT_SECONDS = 3L

        private const val DISABLED_UI_ALPHA = .54f
        private const val ENABLED_UI_ALPHA = 1f
    }
}
