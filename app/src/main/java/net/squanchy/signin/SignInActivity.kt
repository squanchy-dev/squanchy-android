package net.squanchy.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_signin.*
import net.squanchy.R
import net.squanchy.analytics.Analytics
import net.squanchy.google.GoogleClientId
import net.squanchy.support.config.DialogLayoutParameters
import timber.log.Timber

class SignInActivity : AppCompatActivity() {

    private lateinit var service: SignInService
    private lateinit var analytics: Analytics
    private lateinit var googleApiClient: GoogleApiClient

    private var subscription = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ensureSignInOriginIsSet()

        googleApiClient = connectToGoogleApis()

        with(signInComponent(this)) {
            service = service()
            analytics = analytics()
        }

        setContentView(R.layout.activity_signin)

        signInButton.setOnClickListener { signIn() }
        touchOutside.setOnClickListener { finish() }

        setBottomSheetCallbackOn(bottomSheet)

        setupWindowParameters()

        setResult(Activity.RESULT_CANCELED)
    }

    private fun ensureSignInOriginIsSet() {
        if (intent.extras?.containsKey(EXTRA_SIGN_IN_ORIGIN) == false) {
            throw IllegalStateException("Sign in origin extra required but not set")
        }
    }

    private fun connectToGoogleApis(): GoogleApiClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleApiClient.Builder(this)
            .enableAutoManage(this, GoogleClientId.SIGN_IN_ACTIVITY.clientId()) { showSignInFailedError() }
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
            .build()
    }

    private fun setBottomSheetCallbackOn(bottomSheet: View) {
        BottomSheetBehavior.from(bottomSheet)
            .setBottomSheetCallback(bottomSheetCallback)
    }

    private val bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback
        get() = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    finish()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit // No op
        }

    private fun setupWindowParameters() {
        DialogLayoutParameters.wrapHeight(this)
            .applyTo(window)
        window.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result != null && result.isSuccess) {
                val account = result.signInAccount!!
                firebaseAuthWithGoogle(account)
            } else {
                showSignInFailedError()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        showProgress()

        subscription = service.signInWithGoogle(account)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    analytics.trackUserLoggedInFrom(getSignInOrigin())
                    setResult(RESULT_OK)
                    finish()
                },
                Timber::e
            )
    }

    private fun getSignInOrigin() = intent.getSerializableExtra(EXTRA_SIGN_IN_ORIGIN) as SignInOrigin

    private fun showProgress() {
        signInContent.isEnabled = false
        signInContent.alpha = ALPHA_DISABLED
        progressView.isVisible = true
    }

    private fun showSignInFailedError() {
        hideProgress()
        Snackbar.make(signInContent, R.string.sign_in_error_please_retry, Snackbar.LENGTH_SHORT).show()
    }

    private fun hideProgress() {
        signInContent.isEnabled = true
        signInContent.alpha = ALPHA_ENABLED
        progressView.isVisible = false
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onStop() {
        super.onStop()
        subscription.dispose()
    }

    companion object {

        private const val RC_SIGN_IN = 9001
        private const val ALPHA_DISABLED = .54f
        private const val ALPHA_ENABLED = 1f
        const val EXTRA_SIGN_IN_ORIGIN = "sign_in_origin"
    }
}
