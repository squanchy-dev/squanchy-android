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

package net.squanchy.support.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatImageView
import net.squanchy.R

open class ImageViewWithForeground @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), ViewWithForeground {

    private var foregroundDrawable: Drawable? = null
    private var gravity = Gravity.FILL

    private var foregroundInPadding = true
    private var foregroundBoundsChanged: Boolean = false

    private val bounds = Rect()
    private val overlayBounds = Rect()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ImageViewWithForeground, defStyleAttr, 0)

        foregroundGravity = a.getInt(R.styleable.ImageViewWithForeground_android_foregroundGravity, gravity)

        val d = a.getDrawable(R.styleable.ImageViewWithForeground_android_foreground)
        if (d != null) {
            foreground = d
        }

        foregroundInPadding = a.getBoolean(R.styleable.ImageViewWithForeground_foregroundInsidePadding, true)

        a.recycle()
    }

    override fun foregroundGravity(): Int = gravity

    override fun setForegroundGravity(foregroundGravity: Int) {
        if (this.gravity == foregroundGravity) {
            return
        }

        var adjustedGravity = foregroundGravity
        if (adjustedGravity and Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK == 0) {
            adjustedGravity = adjustedGravity or Gravity.START
        }

        if (adjustedGravity and Gravity.VERTICAL_GRAVITY_MASK == 0) {
            adjustedGravity = adjustedGravity or Gravity.TOP
        }

        this.gravity = adjustedGravity

        if (this.gravity == Gravity.FILL && foregroundDrawable != null) {
            val padding = Rect()
            foregroundDrawable!!.getPadding(padding)
        }

        requestLayout()
    }

    override fun verifyDrawable(drawable: Drawable): Boolean {
        return super.verifyDrawable(drawable) || drawable === foregroundDrawable
    }

    override fun jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState()
        foregroundDrawable?.jumpToCurrentState()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (foregroundDrawable != null && foregroundDrawable!!.isStateful) {
            foregroundDrawable!!.state = drawableState
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
    override fun setForeground(drawable: Drawable?) {
        if (foregroundDrawable === drawable || drawable === null) {
            return
        }

        if (foregroundDrawable != null) {
            foregroundDrawable!!.callback = null
            unscheduleDrawable(foregroundDrawable)
        }

        foregroundDrawable = drawable

        drawable.callback = this
        if (drawable.isStateful) {
            drawable.state = drawableState
        }
        if (gravity == Gravity.FILL) {
            val padding = Rect()
            drawable.getPadding(padding)
        }
        requestLayout()
        invalidate()
    }

    /**
     * Returns the drawable used as the foreground of this FrameLayout. The
     * foreground drawable, if non-null, is always drawn on top of the children.
     *
     * @return A Drawable or null if no foreground was set.
     */
    override fun foreground(): Drawable? = foregroundDrawable

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        foregroundBoundsChanged = changed
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (foregroundDrawable == null) {
            return
        }

        val foreground = foregroundDrawable

        if (foregroundBoundsChanged) {
            foregroundBoundsChanged = false
            applyGravityTo(foreground!!)
        }

        foreground!!.draw(canvas)
    }

    private fun applyGravityTo(foreground: Drawable) {
        val selfBounds = bounds

        val w = right - left
        val h = bottom - top

        if (foregroundInPadding) {
            selfBounds.set(0, 0, w, h)
        } else {
            selfBounds.set(paddingLeft, paddingTop, w - paddingRight, h - paddingBottom)
        }

        Gravity.apply(gravity, foreground.intrinsicWidth, foreground.intrinsicHeight, selfBounds, overlayBounds)
        foreground.bounds = overlayBounds
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        foregroundBoundsChanged = true
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun drawableHotspotChanged(x: Float, y: Float) {
        super.drawableHotspotChanged(x, y)
        foregroundDrawable?.setHotspot(x, y)
    }
}
