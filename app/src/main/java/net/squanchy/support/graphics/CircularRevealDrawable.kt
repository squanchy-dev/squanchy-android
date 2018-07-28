package net.squanchy.support.graphics

import android.animation.Animator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.core.animation.doOnEnd
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import me.eugeniomarletti.renderthread.CanvasProperty
import me.eugeniomarletti.renderthread.RenderThread
import kotlin.math.max
import kotlin.math.sqrt

class CircularRevealDrawable : ColorDrawable() {

    private val animationPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val interpolator = FastOutLinearInInterpolator()

    private lateinit var radiusAnimator: Animator

    private lateinit var centerXProperty: CanvasProperty<Float>
    private lateinit var centerYProperty: CanvasProperty<Float>
    private lateinit var radiusProperty: CanvasProperty<Float>
    private lateinit var paintProperty: CanvasProperty<Paint>

    private var startAnimationOnNextDraw: Boolean = false

    private var revealDuration: Int = 0

    private var hotspotX: Float = 0f
    private var hotspotY: Float = 0f
    private var width: Int = 0
    private var height: Int = 0

    @ColorInt
    private var pendingTargetColor: Int = 0

    @ColorInt
    private var targetColor: Int = 0

    fun animateToColor(@ColorInt newColor: Int, @IntRange(from = 0) durationMillis: Int) {
        revealDuration = durationMillis
        pendingTargetColor = newColor
        startAnimationOnNextDraw = true
        color = targetColor
    }

    override fun draw(canvas: Canvas) {
        // 1. Draw the "current" color
        canvas.drawColor(color)

        // 2. If we just got a reveal start request, go with it
        if (startAnimationOnNextDraw) {
            initializeAnimation(canvas, pendingTargetColor)
            radiusAnimator.start()
            startAnimationOnNextDraw = false
        }

        // 3. This draws the reveal, if one is needed
        if (::radiusAnimator.isInitialized && radiusAnimator.isRunning) {
            RenderThread.drawCircle(canvas, centerXProperty, centerYProperty, radiusProperty, paintProperty)
        }
    }

    private fun initializeAnimation(canvas: Canvas, pendingTargetColor: Int) {
        val initialRadius = 0f
        val targetRadius = calculateTargetRadius()
        animationPaint.color = pendingTargetColor

        centerXProperty = RenderThread.createCanvasProperty(canvas, hotspotX)
        centerYProperty = RenderThread.createCanvasProperty(canvas, hotspotY)
        radiusProperty = RenderThread.createCanvasProperty(canvas, initialRadius)
        paintProperty = RenderThread.createCanvasProperty(canvas, animationPaint)

        cancelTransitions()

        radiusAnimator = RenderThread.createFloatAnimator(this, canvas, radiusProperty, targetRadius)
        radiusAnimator.interpolator = interpolator
        radiusAnimator.duration = revealDuration.toLong()
        radiusAnimator.doOnEnd {
            color = targetColor
        }

        targetColor = pendingTargetColor
    }

    private fun calculateTargetRadius(): Float {
        val maxXDistance = max(hotspotX, width - hotspotX)
        val maxYDistance = max(hotspotY, height - hotspotY)
        return sqrt(maxXDistance * maxXDistance + maxYDistance * maxYDistance)
    }

    override fun setHotspot(x: Float, y: Float) {
        super.setHotspot(x, y)
        hotspotX = x
        hotspotY = y
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        width = bounds.width()
        height = bounds.height()
    }

    override fun setColor(color: Int) {
        targetColor = color
        super.setColor(color)
    }

    fun cancelTransitions() {
        if (!::radiusAnimator.isInitialized || !radiusAnimator.isRunning) {
            return
        }

        radiusAnimator.cancel()
        color = targetColor
    }

    companion object {

        fun from(initialColor: Int) = CircularRevealDrawable().apply {
            color = initialColor
            targetColor = initialColor
        }
    }
}
