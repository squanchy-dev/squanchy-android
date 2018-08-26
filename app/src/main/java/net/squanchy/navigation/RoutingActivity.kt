package net.squanchy.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import net.squanchy.R
import net.squanchy.navigation.deeplink.DeepLinkRouter
import net.squanchy.onboarding.Onboarding
import net.squanchy.signin.SignInService
import timber.log.Timber

class RoutingActivity : AppCompatActivity() {

    private lateinit var deepLinkRouter: DeepLinkRouter
    private lateinit var navigator: Navigator
    private lateinit var onboarding: Onboarding
    private lateinit var signInService: SignInService
    private lateinit var firstStartPersister: FirstStartPersister

    private val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = routingComponent(this)
        deepLinkRouter = component.deepLinkRouter()
        navigator = component.navigator()
        onboarding = component.onboarding()
        signInService = component.signInService()
        firstStartPersister = component.firstStartPersister()
    }

    override fun onStart() {
        super.onStart()

        subscriptions.add(
                signInService.signInAnonymouslyIfNecessary()
                    .subscribe(::onboardOrProceedToRouting, ::handleSignInError)
        )
    }

    private fun handleSignInError(throwable: Throwable) {
        Timber.e(throwable, "Error while signing in on routing")
        if (!firstStartPersister.hasBeenStartedAlready()) {
            // We likely have no data here and it'd be a horrible UX, so we show a warning instead
            // to let people know it won't work.
            val continuationIntent = createContinueIntentFrom(intent)
            navigator.toFirstStartWithNoNetwork(continuationIntent)
        } else {
            Toast.makeText(this, R.string.routing_sign_in_unexpected_error, Toast.LENGTH_LONG).show()
        }

        finish()
    }

    private fun createContinueIntentFrom(intent: Intent) =
        Intent(intent).apply {
            removeCategory(Intent.CATEGORY_LAUNCHER)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ONBOARDING_REQUEST_CODE -> handleOnboardingResult(resultCode)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleOnboardingResult(resultCode: Int) {
        when (resultCode) {
            Activity.RESULT_OK -> onboardOrProceedToRouting()
            else -> finish()
        }
    }

    private fun onboardOrProceedToRouting() {
        onboarding.nextPageToShow()
            ?.let { navigator.toOnboardingForResult(it, ONBOARDING_REQUEST_CODE) }
                ?: proceedTo(intent)
    }

    private fun proceedTo(intent: Intent) {
        if (deepLinkRouter.hasDeepLink(intent)) {
            val intentUriString = intent.dataString!!
            Timber.i("Deeplink detected, navigating to $intentUriString")
            deepLinkRouter.navigateTo(intentUriString)
        } else {
            navigator.toHomePage()
        }

        firstStartPersister.storeHasBeenStarted()
        finish()
    }

    override fun onStop() {
        super.onStop()
        subscriptions.clear()
    }

    companion object {

        private const val ONBOARDING_REQUEST_CODE = 2453
    }
}
