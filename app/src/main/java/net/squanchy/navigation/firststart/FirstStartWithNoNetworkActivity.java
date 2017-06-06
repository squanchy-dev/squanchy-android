package net.squanchy.navigation.firststart;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.Property;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.support.config.DialogLayoutParameters;
import net.squanchy.support.lang.Func1;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

@SuppressWarnings("PMD.ExcessiveImports")           // Might use some refactoring later on to extract some collaborator
public class FirstStartWithNoNetworkActivity extends TypefaceStyleableActivity {

    private static final String EXTRA_CONTINUATION_INTENT = FirstStartWithNoNetworkActivity.class.getCanonicalName() + ".continuation_intent";
    private static final long DELAY_AFTER_ANIMATIONS_MILLIS = 700;

    private Intent continuationIntent;

    private ConnectivityManager.NetworkCallback networkCallback;
    private ConnectivityManager connectivityManager;
    private Handler mainThreadHandler;

    private ImageView ctaProgressView;
    private TextView ctaTextView;
    private int shortAnimationDuration;

    public static Intent createIntentContinuingTo(Context context, Intent continuationIntent) {
        Intent intent = new Intent(context, FirstStartWithNoNetworkActivity.class);
        intent.putExtra(EXTRA_CONTINUATION_INTENT, continuationIntent);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_start_with_no_network);

        DialogLayoutParameters.wrapHeight(this)
                .applyTo(getWindow());

        findViewById(R.id.first_start_nevermind_button).setOnClickListener(view -> finish());
        ctaProgressView = (ImageView) findViewById(R.id.cta_progressbar);
        ctaTextView = (TextView) findViewById(R.id.cta_text);

        continuationIntent = getIntent().getParcelableExtra(EXTRA_CONTINUATION_INTENT);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkCallback = new NetworkConnectedCallback();
        mainThreadHandler = new Handler(Looper.getMainLooper());

        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    protected void onStart() {
        super.onStart();

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);

        doMaybeOnAnimatedVector(ctaProgressView.getDrawable(), AnimatedVectorDrawable::start);
    }

    @Override
    protected void onStop() {
        super.onStop();
        connectivityManager.unregisterNetworkCallback(networkCallback);
        doMaybeOnAnimatedVector(ctaProgressView.getDrawable(), AnimatedVectorDrawable::stop);
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException") // The Consumer signature has a throws Exception, we have to catch it
    private void doMaybeOnAnimatedVector(Drawable drawable, Consumer<AnimatedVectorDrawable> action) {
        if (drawable instanceof AnimatedVectorDrawable) {
            try {
                action.accept((AnimatedVectorDrawable) drawable);
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }

    private void showNetworkAcquiredAndFinish() {
        ctaTextView.setText(R.string.first_start_with_no_network_network_connected);

        animate(ctaProgressView, popOut(), this::swapProgressWithSuccessAndContinue)
                .start();
    }

    private Func1<View, Animator> popOut() {
        return view -> {
            Animator alphaAnimator = createFadeAnimationFor(view, 1f, 0f);
            Animator scaleAnimator = createScaleAnimationFor(
                    view,
                    new FastOutLinearInInterpolator(),
                    1f, 0f);
            AnimatorSet animator = new AnimatorSet();
            animator.playTogether(alphaAnimator, scaleAnimator);
            animator.setDuration(shortAnimationDuration);
            return animator;
        };
    }

    private void swapProgressWithSuccessAndContinue() {
        ctaProgressView.setImageResource(R.drawable.ic_circle_tick);

        animate(ctaProgressView, popBackIn(), this::continueToScheduleAfterDelay)
                .start();
    }

    private Func1<View, Animator> popBackIn() {
        return view -> {
            Animator alphaAnimator = createFadeAnimationFor(view, 0f, 1f);
            Animator scaleAnimator = createScaleAnimationFor(
                    view,
                    new BounceInterpolator(),
                    0f, 1f);
            AnimatorSet animator = new AnimatorSet();
            animator.playTogether(alphaAnimator, scaleAnimator);
            animator.setDuration(shortAnimationDuration);
            return animator;
        };
    }

    private Animator createFadeAnimationFor(View view, float... values) {
        Property<View, Float> property = Property.of(View.class, Float.class, "alpha");
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, property, values);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private Animator createScaleAnimationFor(View view, Interpolator interpolator, float... values) {
        Property<View, Float> scaleX = Property.of(View.class, Float.class, "scaleX");
        Property<View, Float> scaleY = Property.of(View.class, Float.class, "scaleY");
        AnimatorSet animator = new AnimatorSet();
        animator.playTogether(
                ObjectAnimator.ofFloat(view, scaleX, values),
                ObjectAnimator.ofFloat(view, scaleY, values)
        );
        animator.setInterpolator(interpolator);
        return animator;
    }

    private Animator animate(View view, Func1<View, Animator> animationProducer, Action endAction) {
        Animator animator = animationProducer.call(view);
        animator.addListener(new AnimationEndListener() {
            @Override
            @SuppressWarnings("PMD.AvoidCatchingGenericException") // The Action signature has a throws Exception, we have to catch it
            public void onAnimationEnd(Animator animation) {
                try {
                    endAction.run();
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        });
        return animator;
    }

    private void continueToScheduleAfterDelay() {
        ctaProgressView.postDelayed(() -> {
            startActivity(continuationIntent);      // We don't use the navigator here, we basically want to restart the whole flow
            finish();
        }, DELAY_AFTER_ANIMATIONS_MILLIS);
    }

    private class NetworkConnectedCallback extends ConnectivityManager.NetworkCallback {

        private boolean receivedOnAvailable;

        @Override
        public void onAvailable(Network network) {
            if (receivedOnAvailable) {
                return;
            }

            receivedOnAvailable = true;

            mainThreadHandler.post(FirstStartWithNoNetworkActivity.this::showNetworkAcquiredAndFinish);
        }
    }

    private static class AnimationEndListener implements Animator.AnimatorListener {

        @Override
        public final void onAnimationStart(Animator animation) {
            // No-op
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // To override
        }

        @Override
        public final void onAnimationCancel(Animator animation) {
            // No-op
        }

        @Override
        public final void onAnimationRepeat(Animator animation) {
            // No-op
        }
    }
}
