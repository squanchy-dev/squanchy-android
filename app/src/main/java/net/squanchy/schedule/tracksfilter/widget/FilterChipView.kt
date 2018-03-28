package net.squanchy.schedule.tracksfilter.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorInt
import android.support.annotation.Px
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Checkable
import android.widget.TextView
import net.squanchy.R
import net.squanchy.support.graphics.pickBestTextColorByContrast

class FilterChipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int = 0
) : TextView(context, attrs, defStyle), Checkable {

    private var isInitialized = false

    private var isChecked: Boolean = false
    private var isBroadcastingCheckedChange: Boolean = false

    private var baseColor: Int
    private var textDarkColor: Int
    private var textLightColor: Int
    @Px
    private var strokeWidth: Int
    private var backgroundDrawable: GradientDrawable
    private var backgroundAlpha: CheckableValue<Float>

    private lateinit var backgroundColor: CheckableValue<Int>
    private lateinit var strokeColor: CheckableValue<Int>
    private lateinit var textColor: CheckableValue<Int>

    init {
        val a = this.context.obtainStyledAttributes(attrs, R.styleable.FilterChipView, defStyle, R.style.Widget_Squanchy_FilterChipView)

        textDarkColor = a.getColor(R.styleable.FilterChipView_checkedDarkTextColor, Color.RED)
        textLightColor = a.getColor(R.styleable.FilterChipView_checkedLightTextColor, Color.MAGENTA)

        baseColor = a.getColor(R.styleable.FilterChipView_color, Color.MAGENTA)
        updateColors(baseColor)

        strokeWidth = a.getDimensionPixelSize(R.styleable.FilterChipView_strokeWidth, 0)

        backgroundDrawable = ResourcesCompat.getDrawable(resources, R.drawable.chip_background, context.theme) as GradientDrawable
        super.setBackground(backgroundDrawable)

        backgroundAlpha = CheckableValue(
            checkedValue = a.getFloat(R.styleable.FilterChipView_checkedAlpha, DEFAULT_CHECKED_ALPHA),
            uncheckedValue = a.getFloat(R.styleable.FilterChipView_uncheckedAlpha, DEFAULT_UNCHECKED_ALPHA)
        )

        a.recycle()

        isClickable = true
        isFocusable = true

        super.setOnClickListener { toggle() }

        isInitialized = true
    }

    var color: Int
        @ColorInt
        get() = baseColor
        set(@ColorInt color) = updateColors(color)

    private fun updateColors(@ColorInt baseColor: Int) {
        this.baseColor = baseColor
        backgroundColor = CheckableValue(checkedValue = baseColor, uncheckedValue = Color.TRANSPARENT)
        strokeColor = CheckableValue(checkedValue = baseColor, uncheckedValue = baseColor)

        val checkedTextColor = baseColor.pickBestTextColorByContrast(textLightColor, textDarkColor)
        val uncheckedTextColor = Color.WHITE.pickBestTextColorByContrast(baseColor, textDarkColor)
        textColor = CheckableValue(checkedValue = checkedTextColor, uncheckedValue = uncheckedTextColor)

        invalidate()
    }

    override fun isChecked(): Boolean = isChecked

    override fun toggle() {
        setChecked(!isChecked)
    }

    override fun setChecked(checked: Boolean) {
        if (checked == isChecked) return
        isChecked = checked

        updateBackgroundDrawable()
        updateTextColor()
        invalidate()

        // Avoid infinite recursions if checked is set from a listener
        if (isBroadcastingCheckedChange) {
            return
        }

        isBroadcastingCheckedChange = true
        onCheckedChangeListener?.invoke(this, checked)
        isBroadcastingCheckedChange = false
    }

    private fun updateBackgroundDrawable() {
        with(backgroundDrawable) {
            setStroke(strokeWidth, strokeColor[isChecked])
            color = ColorStateList.valueOf(backgroundColor[isChecked])
            alpha = (backgroundAlpha[isChecked] * Companion.MAX_ALPHA_VALUE).toInt()
        }

        invalidateOutline()
    }

    private fun updateTextColor() {
        super.setTextColor(textColor[isChecked])
    }

    var onCheckedChangeListener: ((FilterChipView, Boolean) -> Unit)? = null

    override fun getAccessibilityClassName(): CharSequence {
        return FilterChipView::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.isCheckable = true
        info.isChecked = isChecked
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.isChecked = isChecked
    }

    override fun setOnClickListener(l: OnClickListener?) {
        throw UnsupportedOperationException("Can't set a custom click listener on FilterChipView, use onCheckedChangeListener instead")
    }

    override fun setBackgroundTintList(tint: ColorStateList?) {
        when {
            isInitialized -> UnsupportedOperationException("Can't interact with a FilterChipView's background directly")
            else -> super.setBackgroundTintList(tint)
        }
    }

    override fun setBackground(background: Drawable?) {
        when {
            isInitialized -> UnsupportedOperationException("Can't interact with a FilterChipView's background directly")
            else -> super.setBackground(background)
        }
    }

    override fun setBackgroundTintMode(tintMode: PorterDuff.Mode?) {
        when {
            isInitialized -> UnsupportedOperationException("Can't interact with a FilterChipView's background directly")
            else -> super.setBackgroundTintMode(tintMode)
        }
    }

    @Suppress("DEPRECATION") // We're just preventing others from messing with this, not really using it
    override fun setBackgroundDrawable(background: Drawable?) {
        when {
            isInitialized -> UnsupportedOperationException("Can't interact with a FilterChipView's background directly")
            else -> super.setBackgroundDrawable(background)
        }
    }

    override fun setBackgroundResource(resid: Int) {
        when {
            isInitialized -> UnsupportedOperationException("Can't interact with a FilterChipView's background directly")
            else -> super.setBackgroundResource(resid)
        }
    }

    override fun setBackgroundColor(color: Int) {
        when {
            isInitialized -> UnsupportedOperationException("Can't interact with a FilterChipView's background directly")
            else -> super.setBackgroundColor(color)
        }
    }

    override fun getBackgroundTintList(): ColorStateList {
        throw UnsupportedOperationException("Can't interact with a FilterChipView's background directly")
    }

    override fun setTextColor(color: Int) {
        when {
            isInitialized -> throw UnsupportedOperationException("Can't interact with a FilterChipView's text color directly")
            else -> super.setTextColor(color)
        }
    }

    override fun setTextColor(colors: ColorStateList?) {
        when {
            isInitialized -> throw UnsupportedOperationException("Can't interact with a FilterChipView's text color directly")
            else -> super.setTextColor(colors)
        }
    }

    companion object {
        private const val MAX_ALPHA_VALUE: Int = 255
        private const val DEFAULT_CHECKED_ALPHA: Float = 1.0F
        private const val DEFAULT_UNCHECKED_ALPHA: Float = .7F
    }
}

private data class CheckableValue<out T>(private val checkedValue: T, private val uncheckedValue: T) {

    operator fun get(checked: Boolean) = when {
        checked -> checkedValue
        else -> uncheckedValue
    }
}
