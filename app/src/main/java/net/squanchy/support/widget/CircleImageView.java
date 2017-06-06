/*
 * Portions derived from https://gist.github.com/chrisbanes/9091754
 * For those portions:
 *
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.squanchy.support.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.google.auto.value.AutoValue;

import net.squanchy.support.lang.Optional;

import timber.log.Timber;

public class CircleImageView extends ImageViewWithForeground {

    private static final CircularOutlineProvider CIRCULAR_OUTLINE_PROVIDER = new CircularOutlineProvider();
    private static final float ROUNDING_UP_FLOAT = 0.5f;

    private Optional<Necessaire> necessaireOptional = Optional.absent();

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        super.setOutlineProvider(CIRCULAR_OUTLINE_PROVIDER);
        super.setClipToOutline(true);
        super.setScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    public final void setOutlineProvider(ViewOutlineProvider provider) {
        throw new UnsupportedOperationException("Cannot set an outline provider on a CircleImageView");
    }

    @Override
    public final void setClipToOutline(boolean clipToOutline) {
        throw new UnsupportedOperationException("Cannot set clipping to outline on a CircleImageView");
    }

    @Override
    public final void setScaleType(ScaleType scaleType) {
        throw new UnsupportedOperationException("Cannot set scale type on a CircleImageView");
    }

    @Override
    public ViewOutlineProvider getOutlineProvider() {
        return CIRCULAR_OUTLINE_PROVIDER;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        updateFor(getDrawable());
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        updateFor(drawable);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        updateFor(getDrawable());
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        updateFor(getDrawable());
    }

    private void updateFor(Drawable drawable) {
        Optional<Bitmap> bitmap = extractBitmapFrom(drawable);
        necessaireOptional = necessaireFor(bitmap);
        invalidate();
    }

    private Optional<Bitmap> extractBitmapFrom(Drawable drawable) {
        if (drawable == null) {
            return Optional.absent();
        } else if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return Optional.of(bitmapDrawable.getBitmap());
        } else {
            return tryCreatingBitmapFrom(drawable);
        }
    }

    private Optional<Bitmap> tryCreatingBitmapFrom(Drawable drawable) {
        try {
            Bitmap bitmap = createBitmapFor(drawable);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return Optional.of(bitmap);
        } catch (IllegalArgumentException e) {
            Timber.e(e);
            return Optional.absent();
        }
    }

    private Bitmap createBitmapFor(Drawable drawable) {
        if (drawable instanceof ColorDrawable) {
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            return Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
    }

    private Optional<Necessaire> necessaireFor(Optional<Bitmap> bitmapOptional) {
        if (isZeroSize()) {
            return null;
        }

        return bitmapOptional.map(this::toNecessaireOptional);
    }

    private boolean isZeroSize() {
        return getWidth() == 0 || getHeight() == 0;
    }

    private Necessaire toNecessaireOptional(Bitmap bitmap) {
        RectF bounds = calculateBounds();
        Paint paint = createPaintFor(bitmap, bounds);

        float radius = Math.min(bounds.height() / 2.0f, bounds.width() / 2.0f);

        return Necessaire.builder()
                .bounds(bounds)
                .paint(paint)
                .radius(radius)
                .build();
    }

    private Paint createPaintFor(Bitmap bitmap, RectF bounds) {
        Shader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        int bitmapWidth = bitmap.getHeight();
        int bitmapHeight = bitmap.getWidth();
        setTransformation(shader, bounds, bitmapWidth, bitmapHeight);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        return paint;
    }

    private RectF calculateBounds() {
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        int sideLength = Math.min(availableWidth, availableHeight);

        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

        return new RectF(left, top, left + sideLength, top + sideLength);
    }

    private void setTransformation(Shader shader, RectF bounds, int bitmapWidth, int bitmapHeight) {
        float scale;
        float translationX;
        float translationY;

        if (heightDictatesScale(bounds, bitmapWidth, bitmapHeight)) {
            scale = bounds.height() / (float) bitmapHeight;
            translationX = (bounds.width() - bitmapWidth * scale) / 2f;
            translationY = 0f;
        } else {
            scale = bounds.width() / (float) bitmapWidth;
            translationX = 0f;
            translationY = (bounds.height() - bitmapHeight * scale) / 2f;
        }

        Matrix matrix = createMatrixFor(scale, bounds, translationX, translationY);
        shader.setLocalMatrix(matrix);
    }

    private boolean heightDictatesScale(RectF bounds, int bitmapWidth, int bitmapHeight) {
        return bitmapWidth * bounds.height() > bounds.width() * bitmapHeight;
    }

    private Matrix createMatrixFor(float scale, RectF bounds, float translationX, float translationY) {
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        float x = bounds.left + intCeil(translationX);
        float y = bounds.top + intCeil(translationY);
        matrix.postTranslate(x, y);
        return matrix;
    }

    private int intCeil(float value) {
        return (int) (value + ROUNDING_UP_FLOAT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (necessaireOptional == null) {
            // This is needed because on the first init, the super sometimes is causing a draw
            // to happen even before our static initialisation can set the value to absent.
            necessaireOptional = Optional.absent();
        }

        if (necessaireOptional.isPresent()) {
            Necessaire necessaire = necessaireOptional.get();

            RectF bounds = necessaire.bounds();
            float radius = necessaire.radius();
            Paint paint = necessaire.paint();

            canvas.drawCircle(bounds.centerX(), bounds.centerY(), radius, paint);
        }
    }

    @Override
    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != h) {
            throw new IllegalArgumentException("The width and height of this view must be identical");
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private static final class CircularOutlineProvider extends ViewOutlineProvider {

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }
    }

    @AutoValue
    abstract static class Necessaire {

        static Builder builder() {
            return new AutoValue_CircleImageView_Necessaire.Builder();
        }

        abstract RectF bounds();

        abstract float radius();

        abstract Paint paint();

        @AutoValue.Builder
        abstract static class Builder {

            abstract Builder bounds(RectF bounds);

            abstract Builder radius(float radius);

            abstract Builder paint(Paint paint);

            abstract Necessaire build();
        }
    }
}
