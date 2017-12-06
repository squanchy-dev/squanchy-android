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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.Gravity;

import net.squanchy.R;

import org.jetbrains.annotations.NotNull;

public class ImageViewWithForeground extends AppCompatImageView implements ViewWithForeground {

    @Nullable
    private Drawable foregroundDrawable;

    private int foregroundGravity = Gravity.FILL;

    private boolean foregroundInPadding = true;
    private boolean foregroundBoundsChanged;

    private final Rect bounds = new Rect();
    private final Rect overlayBounds = new Rect();

    public ImageViewWithForeground(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewWithForeground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageViewWithForeground,
                defStyleAttr, 0);

        foregroundGravity = a.getInt(
                R.styleable.ImageViewWithForeground_android_foregroundGravity, foregroundGravity);

        Drawable d = a.getDrawable(R.styleable.ImageViewWithForeground_android_foreground);
        if (d != null) {
            setForeground(d);
        }

        foregroundInPadding = a.getBoolean(
                R.styleable.ImageViewWithForeground_foregroundInsidePadding, true);

        a.recycle();
    }

    @Override
    public int foregroundGravity() {
        return foregroundGravity;
    }

    @Override
    public void setForegroundGravity(int foregroundGravity) {
        if (this.foregroundGravity == foregroundGravity) {
            return;
        }

        int adjustedGravity = foregroundGravity;
        if ((adjustedGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
            adjustedGravity |= Gravity.START;
        }

        if ((adjustedGravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
            adjustedGravity |= Gravity.TOP;
        }

        this.foregroundGravity = adjustedGravity;

        if (this.foregroundGravity == Gravity.FILL && foregroundDrawable != null) {
            Rect padding = new Rect();
            foregroundDrawable.getPadding(padding);
        }

        requestLayout();
    }

    @Override
    @SuppressWarnings("PMD.CompareObjectsWithEquals") // We actually want the instance comparison here
    protected boolean verifyDrawable(@NotNull Drawable drawable) {
        return super.verifyDrawable(drawable) || (drawable == foregroundDrawable);
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (foregroundDrawable != null) {
            foregroundDrawable.jumpToCurrentState();
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (foregroundDrawable != null && foregroundDrawable.isStateful()) {
            foregroundDrawable.setState(getDrawableState());
        }
    }

    /**
     * Supply a Drawable that is to be rendered on top of all of the child
     * views in the frame layout.  Any padding in the Drawable will be taken
     * into account by ensuring that the children are inset to be placed
     * inside of the padding area.
     *
     * @param drawable The Drawable to be drawn on top of the children.
     */
    @Override
    @SuppressWarnings("PMD.CompareObjectsWithEquals") // We actually want the instance comparison here
    public final void setForeground(Drawable drawable) {
        if (foregroundDrawable == drawable) {
            return;
        }

        if (foregroundDrawable != null) {
            foregroundDrawable.setCallback(null);
            unscheduleDrawable(foregroundDrawable);
        }

        foregroundDrawable = drawable;

        drawable.setCallback(this);
        if (drawable.isStateful()) {
            drawable.setState(getDrawableState());
        }
        if (foregroundGravity == Gravity.FILL) {
            Rect padding = new Rect();
            drawable.getPadding(padding);
        }
        requestLayout();
        invalidate();
    }

    /**
     * Returns the drawable used as the foreground of this FrameLayout. The
     * foreground drawable, if non-null, is always drawn on top of the children.
     *
     * @return A Drawable or null if no foreground was set.
     */
    @Override
    public Drawable foreground() {
        return foregroundDrawable;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        foregroundBoundsChanged = changed;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (foregroundDrawable == null) {
            return;
        }

        Drawable foreground = foregroundDrawable;

        if (foregroundBoundsChanged) {
            foregroundBoundsChanged = false;
            applyGravityTo(foreground);
        }

        foreground.draw(canvas);
    }

    private void applyGravityTo(Drawable foreground) {
        Rect selfBounds = bounds;

        int w = getRight() - getLeft();
        int h = getBottom() - getTop();

        if (foregroundInPadding) {
            selfBounds.set(0, 0, w, h);
        } else {
            selfBounds.set(
                    getPaddingLeft(),
                    getPaddingTop(),
                    w - getPaddingRight(),
                    h - getPaddingBottom()
            );
        }

        Gravity.apply(
                foregroundGravity,
                foreground.getIntrinsicWidth(),
                foreground.getIntrinsicHeight(),
                selfBounds,
                overlayBounds
        );
        foreground.setBounds(overlayBounds);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        foregroundBoundsChanged = true;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && foregroundDrawable != null) {
            foregroundDrawable.setHotspot(x, y);
        }
    }
}
