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
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.DrawableRes
import timber.log.Timber
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : ImageViewWithForeground(context, attrs, defStyleAttr) {

    private var necessaire: Necessaire? = null

    private val isZeroSize: Boolean
        get() = width == 0 || height == 0

    init {
        super.setOutlineProvider(CircularOutlineProvider)
        super.setClipToOutline(true)
        super.setScaleType(ImageView.ScaleType.CENTER_CROP)
    }

    override fun setOutlineProvider(provider: ViewOutlineProvider): Nothing {
        throw UnsupportedOperationException("Cannot set an outline provider on a CircleImageView")
    }

    override fun setClipToOutline(clipToOutline: Boolean): Nothing {
        throw UnsupportedOperationException("Cannot set clipping to outline on a CircleImageView")
    }

    override fun setScaleType(scaleType: ImageView.ScaleType): Nothing {
        throw UnsupportedOperationException("Cannot set scale type on a CircleImageView")
    }

    override fun getOutlineProvider(): ViewOutlineProvider = CircularOutlineProvider

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        updateFor(drawable)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        updateFor(drawable)
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        updateFor(drawable)
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        updateFor(drawable)
    }

    private fun updateFor(drawable: Drawable?) {
        val bitmap = extractBitmapFrom(drawable)
        necessaire = bitmap?.toNecessaire()
        invalidate()
    }

    private fun extractBitmapFrom(drawable: Drawable?): Bitmap? {
        return when (drawable) {
            null -> null
            is BitmapDrawable -> (drawable as? BitmapDrawable)?.bitmap
            else -> tryCreatingBitmapFrom(drawable)
        }
    }

    private fun tryCreatingBitmapFrom(drawable: Drawable): Bitmap? {
        try {
            val bitmap = createBitmapFor(drawable)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
            return null
        }
    }

    private fun createBitmapFor(drawable: Drawable): Bitmap {
        return if (drawable is ColorDrawable) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }
    }

    private fun Bitmap.toNecessaire(): Necessaire? {
        if (!isZeroSize) {
            val bounds = calculateBounds()
            val paint = createPaintFor(this, bounds)

            val radius = Math.min(bounds.height() / 2.0f, bounds.width() / 2.0f)

            return Necessaire(bounds, radius, paint)
        } else {
            return null
        }
    }

    private fun createPaintFor(bitmap: Bitmap, bounds: RectF): Paint {
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val bitmapWidth = bitmap.height
        val bitmapHeight = bitmap.width
        setTransformation(bitmapShader, bounds, bitmapWidth, bitmapHeight)

        return Paint().apply {
            isAntiAlias = true
            shader = bitmapShader
        }
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }

    private fun setTransformation(shader: Shader, bounds: RectF, bitmapWidth: Int, bitmapHeight: Int) {
        val scale: Float
        val translationX: Float
        val translationY: Float

        if (heightDictatesScale(bounds, bitmapWidth, bitmapHeight)) {
            scale = bounds.height() / bitmapHeight.toFloat()
            translationX = (bounds.width() - bitmapWidth * scale) / 2f
            translationY = 0f
        } else {
            scale = bounds.width() / bitmapWidth.toFloat()
            translationX = 0f
            translationY = (bounds.height() - bitmapHeight * scale) / 2f
        }

        val matrix = createMatrixFor(scale, bounds, translationX, translationY)
        shader.setLocalMatrix(matrix)
    }

    private fun heightDictatesScale(bounds: RectF, bitmapWidth: Int, bitmapHeight: Int): Boolean {
        return bitmapWidth * bounds.height() > bounds.width() * bitmapHeight
    }

    private fun createMatrixFor(scale: Float, bounds: RectF, translationX: Float, translationY: Float): Matrix {
        return Matrix().apply {
            setScale(scale, scale)
            val x = bounds.left + intCeil(translationX)
            val y = bounds.top + intCeil(translationY)
            postTranslate(x, y)
        }
    }

    private fun intCeil(value: Float): Int = (value + ROUNDING_UP_FLOAT).toInt()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        necessaire?.also {
            canvas.drawCircle(it.bounds.centerX(), it.bounds.centerY(), it.radius, it.paint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != h) {
            throw IllegalArgumentException("The width and height of this view must be identical")
        }
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private object CircularOutlineProvider : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) {
            outline.setOval(0, 0, view.width, view.height)
        }
    }

    private data class Necessaire(val bounds: RectF, val radius: Float, val paint: Paint)

    companion object {

        private const val ROUNDING_UP_FLOAT = 0.5f
    }
}
