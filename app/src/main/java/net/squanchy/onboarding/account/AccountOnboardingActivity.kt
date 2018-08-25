package net.squanchy.onboarding.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_onboarding_account.*
import net.squanchy.R
import net.squanchy.navigation.Navigator
import net.squanchy.onboarding.Onboarding
import net.squanchy.onboarding.OnboardingPage
import net.squanchy.signin.SignInOrigin
import net.squanchy.signin.SignInService
import net.squanchy.support.view.enableLightNavigationBar
import java.util.concurrent.TimeUnit

class AccountOnboardingActivity : AppCompatActivity() {

    private lateinit var onboarding: Onboarding
    private lateinit var navigator: Navigator
    private lateinit var signInService: SignInService
    private lateinit var subscription: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(accountOnboardingComponent(this)) {
            onboarding = onboarding()
            navigator = navigator()
            signInService = signInService()
        }

        setContentView(R.layout.activity_onboarding_account)
        enableLightNavigationBar(this)

        onboardingSkip.setOnClickListener { markPageAsSeenAndFinish() }
        onboardingSignInButton.setOnClickListener { signInToGoogle() }

        setResult(Activity.RESULT_CANCELED)
    }

    override fun onStart() {
        super.onStart()

        disableUi()
        subscription = signInService.isSignedInToGoogle()
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(SIGNIN_STATE_CHECK_TIMEOUT_SECONDS, TimeUnit.SECONDS, Schedulers.computation())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { signedIn ->
                    if (signedIn) {
                        markPageAsSeenAndFinish()
                    } else {
                        enableUi()
                    }
                },
                { enableUi() }
            )
    }

    private fun signInToGoogle() {
        disableUi()
        navigator.toSignInForResult(REQUEST_CODE_SIGNIN, SignInOrigin.ONBOARDING)
    }

    private fun disableUi() {
        onboardingContentRoot.isEnabled = false
        onboardingContentRoot.alpha = DISABLED_UI_ALPHA
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
        onboardingContentRoot.isEnabled = true
        onboardingContentRoot.alpha = ENABLED_UI_ALPHA
    }

    override fun onStop() {
        super.onStop()
        subscription.dispose()
    }

    companion object {

        private const val REQUEST_CODE_SIGNIN = 1235
        private const val SIGNIN_STATE_CHECK_TIMEOUT_SECONDS = 3L

        private const val DISABLED_UI_ALPHA = .54f
        private const val ENABLED_UI_ALPHA = 1f
    }
}
