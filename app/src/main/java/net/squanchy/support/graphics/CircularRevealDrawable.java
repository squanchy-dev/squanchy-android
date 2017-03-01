package net.squanchy.support.graphics;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.animation.Interpolator;

import me.eugeniomarletti.renderthread.CanvasProperty;
import me.eugeniomarletti.renderthread.RenderThread;

public class CircularRevealDrawable extends ColorDrawable {

    private final Paint animationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final SimpleAnimationEndListener animationEndListener = new SimpleAnimationEndListener() {
        @Override
        public void onAnimationEnd(Animator animation) {
            setColor(targetColor);
        }
    };
    
    private final Interpolator interpolator = new FastOutLinearInInterpolator();

    private Animator radiusAnimator;

    private CanvasProperty<Float> centerXProperty;
    private CanvasProperty<Float> centerYProperty;
    private CanvasProperty<Float> radiusProperty;
    private CanvasProperty<Paint> paintProperty;

    private boolean startAnimationOnNextDraw;

    private int durationMillis;

    private float hotspotX;
    private float hotspotY;
    private int width;
    private int height;

    @ColorInt
    private int targetColor;

    public static CircularRevealDrawable from(ColorDrawable colorDrawable) {
        CircularRevealDrawable revealDrawable = new CircularRevealDrawable();
        revealDrawable.setColor(colorDrawable.getColor());
        return revealDrawable;
    }

    public void animateToColor(@ColorInt int color, @IntRange(from = 0) int durationMillis) {
        targetColor = color;
        startAnimationOnNextDraw = true;
        this.durationMillis = durationMillis;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        // 1. Let the "main" color be drawn
        super.draw(canvas);

        // 2. If we just got a reveal start request, go with it
        if (startAnimationOnNextDraw) {
            initializeAnimation(canvas);
            radiusAnimator.start();
            startAnimationOnNextDraw = false;
        }

        if (centerXProperty != null && centerYProperty != null && radiusProperty != null && paintProperty != null) {
            RenderThread.drawCircle(canvas, centerXProperty, centerYProperty, radiusProperty, paintProperty);
        }
    }

    private void initializeAnimation(Canvas canvas) {
        float initialRadius = 0f;
        float targetRadius = calculateTargetRadius();
        animationPaint.setColor(targetColor);

        centerXProperty = RenderThread.createCanvasProperty(canvas, hotspotX);
        centerYProperty = RenderThread.createCanvasProperty(canvas, hotspotY);
        radiusProperty = RenderThread.createCanvasProperty(canvas, initialRadius);
        paintProperty = RenderThread.createCanvasProperty(canvas, animationPaint);

        if (radiusAnimator != null) {
            radiusAnimator.cancel();
        }
        radiusAnimator = RenderThread.createFloatAnimator(this, canvas, radiusProperty, targetRadius);
        radiusAnimator.setInterpolator(interpolator);
        radiusAnimator.setDuration(durationMillis);
        radiusAnimator.addListener(animationEndListener);
    }

    private float calculateTargetRadius() {
        float maxXDistance = Math.max(hotspotX, width - hotspotX);
        float maxYDistance = Math.max(hotspotY, height - hotspotY);
        return (float) Math.sqrt(maxXDistance * maxXDistance + maxYDistance * maxYDistance);
    }

    @Override
    public void setHotspot(float x, float y) {
        super.setHotspot(x, y);
        hotspotX = x;
        hotspotY = y;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        width = bounds.width();
        height = bounds.height();
    }

    private abstract static class SimpleAnimationEndListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            // Don't care
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            // Don't care
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            // Don't care
        }
    }
}
