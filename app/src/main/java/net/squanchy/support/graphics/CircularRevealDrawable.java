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

    private final Interpolator interpolator = new FastOutLinearInInterpolator();

    private Animator radiusAnimator;

    private CanvasProperty<Float> centerXProperty;
    private CanvasProperty<Float> centerYProperty;
    private CanvasProperty<Float> radiusProperty;
    private CanvasProperty<Paint> paintProperty;

    private boolean startAnimationOnNextDraw;

    private int revealDuration;

    private float hotspotX;
    private float hotspotY;
    private int width;
    private int height;

    @ColorInt
    private int pendingTargetColor;

    @ColorInt
    private int targetColor;

    public static CircularRevealDrawable from(int initialColor) {
        CircularRevealDrawable revealDrawable = new CircularRevealDrawable();
        revealDrawable.setColor(initialColor);
        revealDrawable.targetColor = initialColor;
        return revealDrawable;
    }

    public void animateToColor(@ColorInt int newColor, @IntRange(from = 0) int durationMillis) {
        revealDuration = durationMillis;
        pendingTargetColor = newColor;
        startAnimationOnNextDraw = true;
        setColor(pendingTargetColor);
    }

    @Override
    public void draw(Canvas canvas) {
        // 1. Draw the "current" color
        canvas.drawColor(getColor());

        // 2. If we just got a reveal start request, go with it
        if (startAnimationOnNextDraw) {
            initializeAnimation(canvas, pendingTargetColor);
            radiusAnimator.start();
            startAnimationOnNextDraw = false;
        }

        // 3. This draws the reveal, if one is needed
        if (radiusAnimator != null && radiusAnimator.isRunning()) {
            RenderThread.drawCircle(canvas, centerXProperty, centerYProperty, radiusProperty, paintProperty);
        }
    }

    private void initializeAnimation(Canvas canvas, int pendingTargetColor) {
        float initialRadius = 0f;
        float targetRadius = calculateTargetRadius();
        animationPaint.setColor(pendingTargetColor);

        centerXProperty = RenderThread.createCanvasProperty(canvas, hotspotX);
        centerYProperty = RenderThread.createCanvasProperty(canvas, hotspotY);
        radiusProperty = RenderThread.createCanvasProperty(canvas, initialRadius);
        paintProperty = RenderThread.createCanvasProperty(canvas, animationPaint);

        cancelTransitions();

        radiusAnimator = RenderThread.createFloatAnimator(this, canvas, radiusProperty, targetRadius);
        radiusAnimator.setInterpolator(interpolator);
        radiusAnimator.setDuration(revealDuration);
        targetColor = pendingTargetColor;
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

    @Override
    public void setColor(int color) {
        targetColor = color;
        super.setColor(color);
    }

    public void cancelTransitions() {
        if (radiusAnimator == null || !radiusAnimator.isRunning()) {
            return;
        }

        radiusAnimator.cancel();
        setColor(targetColor);
    }
}
