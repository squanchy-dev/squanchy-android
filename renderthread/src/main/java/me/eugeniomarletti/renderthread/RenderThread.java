package me.eugeniomarletti.renderthread;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.eugeniomarletti.renderthread.typeannotation.DisplayListCanvas;
import me.eugeniomarletti.renderthread.typeannotation.RenderNodeAnimator;

@SuppressWarnings("unused")
public final class RenderThread {

    private RenderThread() {
    }

    private static RenderThreadDelegate delegate;

    static {
        init(false);
    }

    public static void init(boolean skipAndroidVersionCheck) {
        RenderThreadDelegate delegate = RenderThread.delegate;
        if (delegate == null || !delegate.isSupported()) {
            RenderThreadMethods methods = RenderThreadMethods.create(skipAndroidVersionCheck);
            if (methods != null) {
                RenderThread.delegate = new RenderThreadDelegateHw(methods);
            } else {
                RenderThread.delegate = new RenderThreadDelegate();
            }
        }
    }

    public static boolean isSupported() {
        return delegate.isSupported();
    }

    @NonNull
    public static CanvasProperty<Float> createCanvasProperty(@NonNull Canvas canvas, float initialValue) {
        return createCanvasProperty(canvas, initialValue, true);
    }

    @NonNull
    private static CanvasProperty<Float> createCanvasProperty(@NonNull Canvas canvas, float initialValue, boolean useRenderThread) {
        return delegate.createCanvasProperty(canvas, initialValue, useRenderThread);
    }

    @NonNull
    public static CanvasProperty<Paint> createCanvasProperty(@NonNull Canvas canvas, @NonNull Paint initialValue) {
        return createCanvasProperty(canvas, initialValue, true);
    }

    @NonNull
    private static CanvasProperty<Paint> createCanvasProperty(@NonNull Canvas canvas, @NonNull Paint initialValue, boolean useRenderThread) {
        return delegate.createCanvasProperty(canvas, initialValue, useRenderThread);
    }

    public static boolean isDisplayListCanvas(@NonNull Canvas canvas) {
        return delegate.isDisplayListCanvas(canvas);
    }

    public static void setAnimatorTarget(@RenderNodeAnimator @NonNull Animator animator, @DisplayListCanvas @NonNull Canvas target) {
        delegate.setTarget(animator, target);
    }

    public static void drawCircle(
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Float> cx,
            @NonNull CanvasProperty<Float> cy,
            @NonNull CanvasProperty<Float> radius,
            @NonNull CanvasProperty<Paint> paint) {

        delegate.drawCircle(canvas, cx, cy, radius, paint);
    }

    public static void drawRoundRect(
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Float> left,
            @NonNull CanvasProperty<Float> top,
            @NonNull CanvasProperty<Float> right,
            @NonNull CanvasProperty<Float> bottom,
            @NonNull CanvasProperty<Float> rx,
            @NonNull CanvasProperty<Float> ry,
            @NonNull CanvasProperty<Paint> paint) {

        delegate.drawRoundRect(canvas, left, top, right, bottom, rx, ry, paint);
    }

    @NonNull
    public static Animator createFloatAnimator(
            @NonNull Drawable drawable,
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Float> property,
            float targetValue) {

        return delegate.createFloatAnimator(drawable, canvas, property, targetValue);
    }

    @NonNull
    public static Animator createPaintAlphaAnimator(
            @Nullable Drawable drawable,
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Paint> property,
            @FloatRange(from = 0f, to = 255f) float targetValue) {

        return delegate.createPaintAlphaAnimator(drawable, canvas, property, targetValue);
    }

    @NonNull
    public static Animator createPaintStrokeWidthAnimator(
            @Nullable Drawable drawable,
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Paint> property,
            float targetValue) {

        return delegate.createPaintStrokeWidthAnimator(drawable, canvas, property, targetValue);
    }
}
