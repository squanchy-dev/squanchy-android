package net.squanchy.navigation.firststart

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Property
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import kotlinx.android.synthetic.main.activity_first_start_with_no_network.*
import net.squanchy.R
import net.squanchy.support.config.DialogLayoutParameters

class FirstStartWithNoNetworkActivity : AppCompatActivity() {

    private lateinit var continuationIntent: Intent

    private var networkCallback = NetworkConnectedCallback()
    private var mainThreadHandler = Handler(Looper.getMainLooper())

    private lateinit var connectivityManager: ConnectivityManager

    private var shortAnimationDuration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_first_start_with_no_network)

        DialogLayoutParameters.wrapHeight(this)
            .applyTo(window)

        firstStartNevermind.setOnClickListener { finish() }

        continuationIntent = intent.getParcelableExtra(EXTRA_CONTINUATION_INTENT)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    override fun onStart() {
        super.onStart()

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        doIfAnimatedVectorDrawable(firstStartProgress.drawable) { start() }
    }

    override fun onStop() {
        super.onStop()
        connectivityManager.unregisterNetworkCallback(networkCallback)
        doIfAnimatedVectorDrawable(firstStartProgress.drawable) { stop() }
    }

    private fun doIfAnimatedVectorDrawable(drawable: Drawable, action: AnimatedVectorDrawable.() -> Unit) {
        if (drawable is AnimatedVectorDrawable) {
            action.invoke(drawable)
        }
    }

    private fun showNetworkAcquiredAndFinish() {
        firstStartCta.setText(R.string.first_start_with_no_network_network_connected)

        animate(firstStartProgress, popOut(), ::swapProgressWithSuccessAndContinue)
            .start()
    }

    private fun popOut(): (View) -> Animator = {
        AnimatorSet().apply {
            val alphaAnimator = createFadeAnimationFor(it, 1f, 0f)
            val scaleAnimator = createScaleAnimationFor(it, FastOutLinearInInterpolator(), 1f, 0f)
            playTogether(alphaAnimator, scaleAnimator)
            duration = shortAnimationDuration.toLong()
        }
    }

    private fun swapProgressWithSuccessAndContinue() {
        firstStartProgress.setImageResource(R.drawable.ic_circle_tick)

        animate(firstStartProgress, popBackIn()) { this.continueToScheduleAfterDelay() }
            .start()
    }

    private fun popBackIn(): (View) -> Animator = {
        AnimatorSet().apply {
            val alphaAnimator = createFadeAnimationFor(it, 0f, 1f)
            val scaleAnimator = createScaleAnimationFor(it, BounceInterpolator(), 0f, 1f)
            playTogether(alphaAnimator, scaleAnimator)
            duration = shortAnimationDuration.toLong()
        }
    }

    @Suppress("SpreadOperator") // We cannot avoid using the spread operator here, we use varargs APIs
    private fun createFadeAnimationFor(view: View, vararg values: Float): Animator {
        val property = Property.of(View::class.java, Float::class.java, "alpha")
        val animator = ObjectAnimator.ofFloat(view, property, *values)
        animator.interpolator = LinearInterpolator()
        return animator
    }

    @Suppress("SpreadOperator") // We cannot avoid using the spread operator here, we use varargs APIs
    private fun createScaleAnimationFor(view: View, interpolator: Interpolator, vararg values: Float): Animator {
        val scaleX = Property.of(View::class.java, Float::class.java, "scaleX")
        val scaleY = Property.of(View::class.java, Float::class.java, "scaleY")
        val animator = AnimatorSet()
        animator.playTogether(
                ObjectAnimator.ofFloat(view, scaleX, *values),
                ObjectAnimator.ofFloat(view, scaleY, *values)
        )
        animator.interpolator = interpolator
        return animator
    }

    private fun animate(view: View, animationProducer: (View) -> Animator, endAction: () -> Unit): Animator {
        val animator = animationProducer.invoke(view)
        animator.addListener(
                object : AnimationEndListener {
                    override fun onAnimationEnd(animation: Animator) = endAction()
                }
        )
        return animator
    }

    private fun continueToScheduleAfterDelay() {
        firstStartProgress.postDelayed(
                {
                    startActivity(continuationIntent) // We don't use the navigator here, we basically want to restart the whole flow
                    finish()
                }, DELAY_AFTER_ANIMATIONS_MILLIS
        )
    }

    private inner class NetworkConnectedCallback : ConnectivityManager.NetworkCallback() {

        private var receivedOnAvailable: Boolean = false

        override fun onAvailable(network: Network) {
            if (receivedOnAvailable) {
                return
            }

            receivedOnAvailable = true

            mainThreadHandler.post { this@FirstStartWithNoNetworkActivity.showNetworkAcquiredAndFinish() }
        }
    }

    private interface AnimationEndListener : Animator.AnimatorListener {

        override fun onAnimationStart(animation: Animator) {
            // No-op
        }

        override fun onAnimationCancel(animation: Animator) {
            // No-op
        }

        override fun onAnimationRepeat(animation: Animator) {
            // No-op
        }
    }

    companion object {

        private val EXTRA_CONTINUATION_INTENT = "${FirstStartWithNoNetworkActivity::class.java.name}.continuation_intent"
        private const val DELAY_AFTER_ANIMATIONS_MILLIS: Long = 700

        fun createIntentContinuingTo(context: Context, continuationIntent: Intent) =
            Intent(context, FirstStartWithNoNetworkActivity::class.java).apply {
                putExtra(EXTRA_CONTINUATION_INTENT, continuationIntent)
            }
    }
}
