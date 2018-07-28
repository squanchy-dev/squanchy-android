package me.eugeniomarletti.renderthread;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.eugeniomarletti.renderthread.typeannotation.DisplayListCanvas;
import me.eugeniomarletti.renderthread.typeannotation.RenderNodeAnimator;

class RenderThreadDelegate {

    RenderThreadDelegate() {
        // Package private constructor
    }

    public boolean isSupported() {
        return false;
    }

    @NonNull
    public CanvasProperty<Float> createCanvasProperty(@NonNull Canvas canvas, float initialValue, boolean useRenderThread) {
        CanvasProperty<Float> hw = null;
        if (useRenderThread && isDisplayListCanvas(canvas)) {
            hw = createHardwareCanvasProperty(initialValue);
        }
        if (hw != null) {
            return hw;
        } else {
            return createSoftwareCanvasProperty(initialValue);
        }
    }

    @NonNull
    public CanvasProperty<Paint> createCanvasProperty(@NonNull Canvas canvas, @NonNull Paint initialValue, boolean useRenderThread) {
        CanvasProperty<Paint> hw = null;
        if (useRenderThread && isDisplayListCanvas(canvas)) {
            hw = createHardwareCanvasProperty(initialValue);
        }
        if (hw != null) {
            return hw;
        } else {
            return createSoftwareCanvasProperty(initialValue);
        }
    }

    @Nullable
    protected HardwareCanvasProperty<Float> createHardwareCanvasProperty(float initialValue) {
        return null;
    }

    @Nullable
    protected HardwareCanvasProperty<Paint> createHardwareCanvasProperty(@NonNull Paint initialValue) {
        return null;
    }

    @NonNull
    private SoftwareCanvasProperty<Float> createSoftwareCanvasProperty(float initialValue) {
        return new SoftwareCanvasProperty<>(initialValue);
    }

    @NonNull
    private SoftwareCanvasProperty<Paint> createSoftwareCanvasProperty(@NonNull Paint initialValue) {
        return new SoftwareCanvasProperty<>(initialValue);
    }

    public boolean isDisplayListCanvas(@NonNull Canvas canvas) {
        return false;
    }

    public void setTarget(@RenderNodeAnimator @NonNull Animator animator, @DisplayListCanvas @NonNull Canvas target) {
        throw new UnsupportedOperationException();
    }

    public void drawCircle(
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Float> cx,
            @NonNull CanvasProperty<Float> cy,
            @NonNull CanvasProperty<Float> radius,
            @NonNull CanvasProperty<Paint> paint
    ) {
        boolean cxHw = cx.isHardware();
        boolean cyHw = cy.isHardware();
        boolean radiusHw = radius.isHardware();
        boolean paintHw = paint.isHardware();

        boolean allHw = cxHw && cyHw && radiusHw && paintHw;
        if (allHw) {
            drawCircleHardware(
                    canvas,
                    (HardwareCanvasProperty<Float>) cx,
                    (HardwareCanvasProperty<Float>) cy,
                    (HardwareCanvasProperty<Float>) radius,
                    (HardwareCanvasProperty<Paint>) paint
            );
            return;
        }

        boolean allSw = !cxHw && !cyHw && !radiusHw && !paintHw;
        if (allSw) {
            drawCircleSoftware(
                    canvas,
                    (SoftwareCanvasProperty<Float>) cx,
                    (SoftwareCanvasProperty<Float>) cy,
                    (SoftwareCanvasProperty<Float>) radius,
                    (SoftwareCanvasProperty<Paint>) paint
            );
            return;
        }

        throwMixedPropertiesException();
    }

    protected void drawCircleHardware(
            @DisplayListCanvas @NonNull Canvas canvas,
            @NonNull HardwareCanvasProperty<Float> cx,
            @NonNull HardwareCanvasProperty<Float> cy,
            @NonNull HardwareCanvasProperty<Float> radius,
            @NonNull HardwareCanvasProperty<Paint> paint
    ) {
        throw new UnsupportedOperationException();
    }

    private void drawCircleSoftware(
            @NonNull Canvas canvas,
            @NonNull SoftwareCanvasProperty<Float> cx,
            @NonNull SoftwareCanvasProperty<Float> cy,
            @NonNull SoftwareCanvasProperty<Float> radius,
            @NonNull SoftwareCanvasProperty<Paint> paint
    ) {
        canvas.drawCircle(cx.getValue(), cy.getValue(), radius.getValue(), paint.getValue());
    }

    public void drawRoundRect(
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Float> left,
            @NonNull CanvasProperty<Float> top,
            @NonNull CanvasProperty<Float> right,
            @NonNull CanvasProperty<Float> bottom,
            @NonNull CanvasProperty<Float> rx,
            @NonNull CanvasProperty<Float> ry,
            @NonNull CanvasProperty<Paint> paint
    ) {
        boolean leftHw = left.isHardware();
        boolean topHw = top.isHardware();
        boolean rightHw = right.isHardware();
        boolean bottomHw = bottom.isHardware();
        boolean rxHw = rx.isHardware();
        boolean ryHw = ry.isHardware();
        boolean paintHw = paint.isHardware();

        boolean allHw = leftHw && topHw && rightHw && bottomHw && rxHw && ryHw && paintHw;
        if (allHw) {
            drawRoundRectHardware(
                    canvas,
                    (HardwareCanvasProperty<Float>) left,
                    (HardwareCanvasProperty<Float>) top,
                    (HardwareCanvasProperty<Float>) right,
                    (HardwareCanvasProperty<Float>) bottom,
                    (HardwareCanvasProperty<Float>) rx,
                    (HardwareCanvasProperty<Float>) ry,
                    (HardwareCanvasProperty<Paint>) paint
            );
            return;
        }

        boolean allSw = !leftHw && !topHw && !rightHw && !bottomHw && !rxHw && !ryHw && !paintHw;
        if (allSw) {
            drawRoundRectSoftware(
                    canvas,
                    (SoftwareCanvasProperty<Float>) left,
                    (SoftwareCanvasProperty<Float>) top,
                    (SoftwareCanvasProperty<Float>) right,
                    (SoftwareCanvasProperty<Float>) bottom,
                    (SoftwareCanvasProperty<Float>) rx,
                    (SoftwareCanvasProperty<Float>) ry,
                    (SoftwareCanvasProperty<Paint>) paint
            );
            return;
        }

        throwMixedPropertiesException();
    }

    protected void drawRoundRectHardware(
            @DisplayListCanvas @NonNull Canvas canvas,
            @NonNull HardwareCanvasProperty<Float> left,
            @NonNull HardwareCanvasProperty<Float> top,
            @NonNull HardwareCanvasProperty<Float> right,
            @NonNull HardwareCanvasProperty<Float> bottom,
            @NonNull HardwareCanvasProperty<Float> rx,
            @NonNull HardwareCanvasProperty<Float> ry,
            @NonNull HardwareCanvasProperty<Paint> paint
    ) {
        throw new UnsupportedOperationException();
    }

    private void drawRoundRectSoftware(
            @NonNull Canvas canvas,
            @NonNull SoftwareCanvasProperty<Float> left,
            @NonNull SoftwareCanvasProperty<Float> top,
            @NonNull SoftwareCanvasProperty<Float> right,
            @NonNull SoftwareCanvasProperty<Float> bottom,
            @NonNull SoftwareCanvasProperty<Float> rx,
            @NonNull SoftwareCanvasProperty<Float> ry,
            @NonNull SoftwareCanvasProperty<Paint> paint
    ) {
        CanvasUtils.drawRoundRect(
                canvas, left.getValue(), top.getValue(), right.getValue(), bottom.getValue(), rx.getValue(), ry.getValue(), paint.getValue());
    }

    private void throwMixedPropertiesException() {
        throw new IllegalArgumentException("Properties must be either all software or all hardware.");
    }

    @NonNull
    public Animator createFloatAnimator(
            @NonNull Drawable drawable,
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Float> property,
            float targetValue
    ) {
        if (property.isHardware()) {
            return createHardwareFloatAnimator(canvas, (HardwareCanvasProperty<Float>) property, targetValue);
        } else {
            return createSoftwareFloatAnimator(drawable, (SoftwareCanvasProperty<Float>) property, targetValue);
        }
    }

    @NonNull
    public Animator createPaintAlphaAnimator(
            @Nullable Drawable drawable,
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Paint> property,
            @FloatRange(from = 0f, to = 255f) float targetValue
    ) {
        if (property.isHardware()) {
            return createHardwarePaintAlphaAnimator(canvas, (HardwareCanvasProperty<Paint>) property, targetValue);
        } else {
            return createSoftwarePaintAlphaAnimator(drawable, (SoftwareCanvasProperty<Paint>) property, Math.round(targetValue));
        }
    }

    @NonNull
    public Animator createPaintStrokeWidthAnimator(
            @Nullable Drawable drawable,
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Paint> property,
            float targetValue
    ) {
        if (property.isHardware()) {
            return createHardwarePaintStrokeWidthAnimator(canvas, (HardwareCanvasProperty<Paint>) property, targetValue);
        } else {
            return createSoftwarePaintStrokeWidthAnimator(drawable, (SoftwareCanvasProperty<Paint>) property, targetValue);
        }
    }

    @NonNull
    protected Animator createHardwareFloatAnimator(
            @DisplayListCanvas @Nullable Canvas canvas, @NonNull HardwareCanvasProperty<Float> property, float targetValue
    ) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    protected Animator createHardwarePaintAlphaAnimator(
            @DisplayListCanvas @Nullable Canvas canvas,
            @NonNull HardwareCanvasProperty<Paint> property,
            @FloatRange(from = 0f, to = 255f) float targetValue
    ) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    protected Animator createHardwarePaintStrokeWidthAnimator(
            @DisplayListCanvas @Nullable Canvas canvas, @NonNull HardwareCanvasProperty<Paint> property, float targetValue
    ) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    private Animator createSoftwareFloatAnimator(
            @Nullable final Drawable drawable,
            @NonNull final SoftwareCanvasProperty<Float> property,
            float targetValue
    ) {
        ValueAnimator animator = ValueAnimator.ofFloat(property.getValue(), targetValue);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                property.setValue((float) animation.getAnimatedValue());
                if (drawable != null) {
                    drawable.invalidateSelf();
                }
            }
        });
        return animator;
    }

    @NonNull
    private Animator createSoftwarePaintAlphaAnimator(
            @Nullable final Drawable drawable,
            @NonNull final SoftwareCanvasProperty<Paint> property,
            @IntRange(from = 0, to = 255) int targetValue
    ) {
        ValueAnimator animator = ValueAnimator.ofInt(property.getValue().getAlpha(), Math.round(targetValue));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                property.getValue().setAlpha((int) animation.getAnimatedValue());
                if (drawable != null) {
                    drawable.invalidateSelf();
                }
            }
        });
        return animator;
    }

    @NonNull
    private Animator createSoftwarePaintStrokeWidthAnimator(
            @Nullable final Drawable drawable,
            @NonNull final SoftwareCanvasProperty<Paint> property,
            float targetValue
    ) {
        ValueAnimator animator = ValueAnimator.ofFloat(property.getValue().getStrokeWidth(), targetValue);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                property.getValue().setStrokeWidth((float) animation.getAnimatedValue());
                if (drawable != null) {
                    drawable.invalidateSelf();
                }
            }
        });
        return animator;
    }
}
