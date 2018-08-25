package net.squanchy.schedule.tracksfilter.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Checkable
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import net.squanchy.R
import net.squanchy.support.graphics.darkenToEnsureTextContrasts
import net.squanchy.support.graphics.pickBestTextColorByContrast

class FilterChipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle), Checkable {

    private var isInitialized = false

    private var isChecked: Boolean = false
    private var isBroadcastingCheckedChange: Boolean = false

    private var baseColor: Int
    private var checkedTextColorDark: Int
    private var checkedTextColorLight: Int
    private var uncheckedFallbackTextColor: Int

    @Px
    private var strokeWidth: Int

    // It sounds weird, but a <shape> with a stroke color inflates as a GradientDrawable
    private var backgroundDrawable: GradientDrawable
    private var backgroundAlpha: CheckableValue<Float>

    private lateinit var backgroundColor: CheckableValue<Int>
    private lateinit var strokeColor: CheckableValue<Int>
    private lateinit var textColor: CheckableValue<Int>

    init {
        val a = this.context.obtainStyledAttributes(attrs, R.styleable.FilterChipView, defStyle, R.style.Widget_Squanchy_FilterChipView)

        // TODO use more appropriate defaults once finished dev'ing
        checkedTextColorDark = a.getColor(R.styleable.FilterChipView_checkedTextColorDark, Color.YELLOW)
        checkedTextColorLight = a.getColor(R.styleable.FilterChipView_checkedTextColorLight, Color.MAGENTA)
        uncheckedFallbackTextColor = a.getColor(R.styleable.FilterChipView_uncheckedFallbackTextColor, Color.GREEN)

        baseColor = a.getColor(R.styleable.FilterChipView_color, ContextCompat.getColor(context, R.color.chip_default_background_tint))

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

        updateColors(baseColor)
    }

    var color: Int
        @ColorInt
        get() = baseColor
        set(@ColorInt color) = updateColors(color)

    private val parentBackgroundColor
        get() = ASSUMED_PARENT_BACKGROUND_COLOR // TODO allow setting a proper parent color via code if necessary

    private fun updateColors(@ColorInt baseColor: Int) {
        this.baseColor = baseColor
        backgroundColor = CheckableValue(checkedValue = baseColor, uncheckedValue = Color.TRANSPARENT)
        strokeColor = CheckableValue(checkedValue = baseColor, uncheckedValue = baseColor)

        val checkedTextColor = baseColor.pickBestTextColorByContrast(checkedTextColorLight, checkedTextColorDark)
        val contrastSafeBaseColor = baseColor.darkenToEnsureTextContrasts(parentBackgroundColor, uncheckedFallbackTextColor)
        val uncheckedTextColor = parentBackgroundColor.pickBestTextColorByContrast(baseColor, contrastSafeBaseColor)
        textColor = CheckableValue(checkedValue = checkedTextColor, uncheckedValue = uncheckedTextColor)

        redraw()
    }

    override fun isChecked(): Boolean = isChecked

    override fun toggle() {
        setChecked(!isChecked)
    }

    override fun setChecked(checked: Boolean) {
        if (checked == isChecked) return
        isChecked = checked

        redraw()

        // Avoid infinite recursions if checked is set from a listener
        if (isBroadcastingCheckedChange) {
            return
        }

        isBroadcastingCheckedChange = true
        onCheckedChangeListener?.invoke(this, checked)
        isBroadcastingCheckedChange = false
    }

    private fun redraw() {
        updateBackgroundDrawable()
        updateTextColor()
        invalidate()
    }

    private fun updateBackgroundDrawable() {
        backgroundDrawable.apply {
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
        allowOnlyWhileInitializing("Can't interact with a FilterChipView's background directly") {
            super.setBackgroundTintList(tint)
        }
    }

    override fun setBackground(background: Drawable?) {
        allowOnlyWhileInitializing("Can't interact with a FilterChipView's background directly") {
            super.setBackground(background)
        }
    }

    override fun setBackgroundTintMode(tintMode: PorterDuff.Mode?) {
        allowOnlyWhileInitializing("Can't interact with a FilterChipView's background directly") {
            super.setBackgroundTintMode(tintMode)
        }
    }

    @Suppress("DEPRECATION", "OverridingDeprecatedMember") // We're just preventing others from messing with this, not really using it
    override fun setBackgroundDrawable(background: Drawable?) {
        allowOnlyWhileInitializing("Can't interact with a FilterChipView's background directly") {
            super.setBackgroundDrawable(background)
        }
    }

    override fun setBackgroundResource(resid: Int) {
        allowOnlyWhileInitializing("Can't interact with a FilterChipView's background directly") {
            super.setBackgroundResource(resid)
        }
    }

    override fun setBackgroundColor(color: Int) {
        allowOnlyWhileInitializing("Can't interact with a FilterChipView's background directly") {
            super.setBackgroundColor(color)
        }
    }

    override fun getBackgroundTintList(): ColorStateList? {
        return allowOnlyWhileInitializing("Can't obtain the background color of a FilterChipView, use the color property") {
            return@allowOnlyWhileInitializing super.getBackgroundTintList()
        }
    }

    override fun setTextColor(color: Int) {
        allowOnlyWhileInitializing("Can't interact with a FilterChipView's text color directly") {
            super.setTextColor(color)
        }
    }

    override fun setTextColor(colors: ColorStateList?) {
        allowOnlyWhileInitializing("Can't interact with a FilterChipView's text color directly") {
            super.setTextColor(colors)
        }
    }

    private inline fun <T> allowOnlyWhileInitializing(errorMessage: String, whileInitializing: () -> T): T {
        when {
            isInitialized -> throw UnsupportedOperationException(errorMessage)
            else -> return whileInitializing.invoke()
        }
    }

    companion object {
        private const val MAX_ALPHA_VALUE: Int = 255
        private const val DEFAULT_CHECKED_ALPHA: Float = 1.0F
        private const val DEFAULT_UNCHECKED_ALPHA: Float = .7F
        private const val ASSUMED_PARENT_BACKGROUND_COLOR = Color.WHITE
    }
}

private data class CheckableValue<out T>(private val checkedValue: T, private val uncheckedValue: T) {

    operator fun get(checked: Boolean) = when {
        checked -> checkedValue
        else -> uncheckedValue
    }
}
