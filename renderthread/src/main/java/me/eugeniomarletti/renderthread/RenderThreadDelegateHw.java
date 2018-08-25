package me.eugeniomarletti.renderthread;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.eugeniomarletti.renderthread.typeannotation.DisplayListCanvas;
import me.eugeniomarletti.renderthread.typeannotation.RenderNodeAnimator;

final class RenderThreadDelegateHw extends RenderThreadDelegate {

    @NonNull
    private final RenderThreadMethods renderThread;
    
    RenderThreadDelegateHw(@NonNull RenderThreadMethods renderThread) {
        this.renderThread = renderThread;
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @NonNull
    @Override
    protected HardwareCanvasProperty<Float> createHardwareCanvasProperty(float initialValue) {
        return new HardwareCanvasProperty<>(renderThread.createCanvasProperty(initialValue));
    }

    @NonNull
    @Override
    protected HardwareCanvasProperty<Paint> createHardwareCanvasProperty(@NonNull Paint initialValue) {
        return new HardwareCanvasProperty<>(renderThread.createCanvasProperty(initialValue));
    }

    @Override
    public boolean isDisplayListCanvas(@NonNull Canvas canvas) {
        return canvas.isHardwareAccelerated() && renderThread.instanceOfDisplayListCanvas(canvas);
    }

    private void ensureDisplayListCanvas(@NonNull Canvas canvas) {
        if (!isDisplayListCanvas(canvas)) {
            throw new IllegalArgumentException("Canvas is not hardware accelerated.");
        }
    }

    @Override
    public void setTarget(@RenderNodeAnimator @NonNull Animator animator, @DisplayListCanvas @NonNull Canvas target) {
        ensureDisplayListCanvas(target);
        renderThread.setTarget(animator, target);
    }

    @Override
    protected void drawCircleHardware(
            @DisplayListCanvas @NonNull Canvas canvas,
            @NonNull HardwareCanvasProperty<Float> cx,
            @NonNull HardwareCanvasProperty<Float> cy,
            @NonNull HardwareCanvasProperty<Float> radius,
            @NonNull HardwareCanvasProperty<Paint> paint) {

        ensureDisplayListCanvas(canvas);
        renderThread.drawCircle(canvas, cx.getProperty(), cy.getProperty(), radius.getProperty(), paint.getProperty());
    }

    @Override
    protected void drawRoundRectHardware(
            @DisplayListCanvas @NonNull Canvas canvas,
            @NonNull HardwareCanvasProperty<Float> left,
            @NonNull HardwareCanvasProperty<Float> top,
            @NonNull HardwareCanvasProperty<Float> right,
            @NonNull HardwareCanvasProperty<Float> bottom,
            @NonNull HardwareCanvasProperty<Float> rx,
            @NonNull HardwareCanvasProperty<Float> ry,
            @NonNull HardwareCanvasProperty<Paint> paint) {

        ensureDisplayListCanvas(canvas);
        renderThread.drawRoundRect(
                canvas,
                left.getProperty(),
                top.getProperty(),
                right.getProperty(),
                bottom.getProperty(),
                rx.getProperty(),
                ry.getProperty(),
                paint.getProperty());
    }

    @NonNull
    @Override
    protected Animator createHardwareFloatAnimator(
            @DisplayListCanvas @Nullable Canvas canvas, @NonNull HardwareCanvasProperty<Float> property, float targetValue) {

        Animator animator = renderThread.createFloatRenderNodeAnimator(property.getProperty(), targetValue);
        if (canvas != null) {
            setTarget(animator, canvas);
        }
        return animator;
    }

    @NonNull
    @Override
    protected Animator createHardwarePaintAlphaAnimator(
            @DisplayListCanvas @Nullable Canvas canvas,
            @NonNull HardwareCanvasProperty<Paint> property,
            @FloatRange(from = 0f, to = 255f) float targetValue) {

        Animator animator = renderThread.createPaintAlphaRenderNodeAnimator(property.getProperty(), targetValue);
        if (canvas != null) {
            setTarget(animator, canvas);
        }
        return animator;
    }

    @NonNull
    @Override
    protected Animator createHardwarePaintStrokeWidthAnimator(
            @DisplayListCanvas @Nullable Canvas canvas, @NonNull HardwareCanvasProperty<Paint> property, float targetValue) {

        Animator animator = renderThread.createPaintStrokeWidthRenderNodeAnimator(property.getProperty(), targetValue);
        if (canvas != null) {
            setTarget(animator, canvas);
        }
        return animator;
    }
}
