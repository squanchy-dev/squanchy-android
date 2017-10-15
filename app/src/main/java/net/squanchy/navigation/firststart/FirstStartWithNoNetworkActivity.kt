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
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.util.Property
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_first_start_with_no_network.cta_progressbar
import kotlinx.android.synthetic.main.activity_first_start_with_no_network.cta_text
import kotlinx.android.synthetic.main.activity_first_start_with_no_network.first_start_nevermind_button
import net.squanchy.R
import net.squanchy.fonts.TypefaceStyleableActivity
import net.squanchy.support.config.DialogLayoutParameters
import timber.log.Timber

class FirstStartWithNoNetworkActivity : TypefaceStyleableActivity() {

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

        first_start_nevermind_button.setOnClickListener { finish() }

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

        doMaybeOnAnimatedVector(cta_progressbar.drawable, Consumer { it.start() })
    }

    override fun onStop() {
        super.onStop()
        connectivityManager.unregisterNetworkCallback(networkCallback)
        doMaybeOnAnimatedVector(cta_progressbar.drawable, Consumer { it.stop() })
    }

    private// The Consumer signature has a throws Exception, we have to catch it
    fun doMaybeOnAnimatedVector(drawable: Drawable, action: Consumer<AnimatedVectorDrawable>) {
        if (drawable is AnimatedVectorDrawable) {
            try {
                action.accept(drawable)
            } catch (e: Exception) {
                Timber.e(e)
            }

        }
    }

    private fun showNetworkAcquiredAndFinish() {
        cta_text.setText(R.string.first_start_with_no_network_network_connected)

        animate(cta_progressbar, popOut(), Action { this.swapProgressWithSuccessAndContinue() })
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
        cta_progressbar.setImageResource(R.drawable.ic_circle_tick)

        animate(cta_progressbar, popBackIn(), Action { this.continueToScheduleAfterDelay() })
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

    private fun createFadeAnimationFor(view: View, vararg values: Float): Animator {
        val property = Property.of(View::class.java, Float::class.java, "alpha")
        val animator = ObjectAnimator.ofFloat(view, property, *values)
        animator.interpolator = LinearInterpolator()
        return animator
    }

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

    private fun animate(view: View, animationProducer: (View) -> Animator, endAction: Action): Animator {
        val animator = animationProducer.invoke(view)
        animator.addListener(object : AnimationEndListener {
            override fun onAnimationEnd(animation: Animator) =
                    try {
                        endAction.run()
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
        })
        return animator
    }

    private fun continueToScheduleAfterDelay() {
        cta_progressbar.postDelayed({
            startActivity(continuationIntent)      // We don't use the navigator here, we basically want to restart the whole flow
            finish()
        }, DELAY_AFTER_ANIMATIONS_MILLIS)
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

        private val EXTRA_CONTINUATION_INTENT = FirstStartWithNoNetworkActivity::class.java.canonicalName + ".continuation_intent"
        private const val DELAY_AFTER_ANIMATIONS_MILLIS: Long = 700

        fun createIntentContinuingTo(context: Context, continuationIntent: Intent) =
                Intent(context, FirstStartWithNoNetworkActivity::class.java).apply {
                    putExtra(EXTRA_CONTINUATION_INTENT, continuationIntent)
                }
    }
}
