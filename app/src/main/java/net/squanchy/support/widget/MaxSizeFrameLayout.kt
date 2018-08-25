package net.squanchy.support.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.Px
import net.squanchy.R

class MaxSizeFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    @Px
    private var maxWidth: Int = 0
        set(@Px value) {
            field = if (value <= 0) NO_CONSTRAINTS else value
        }

    @Px
    private var maxHeight: Int = 0
        set(@Px value) {
            field = if (value <= 0) NO_CONSTRAINTS else value
        }

    init {

        val a = context.obtainStyledAttributes(attrs, R.styleable.MaxSizeFrameLayout, defStyleAttr, defStyleRes)

        try {
            maxWidth = a.getDimensionPixelSize(R.styleable.MaxSizeFrameLayout_android_maxWidth, NO_CONSTRAINTS)
            maxHeight = a.getDimensionPixelSize(R.styleable.MaxSizeFrameLayout_android_maxHeight, NO_CONSTRAINTS)
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var constrainedWidthSpec = widthMeasureSpec
        var constrainedHeightSpec = heightMeasureSpec

        if (View.MeasureSpec.getSize(widthMeasureSpec) > maxWidth) {
            constrainedWidthSpec = View.MeasureSpec.makeMeasureSpec(maxWidth, View.MeasureSpec.EXACTLY)
        }
        if (View.MeasureSpec.getSize(heightMeasureSpec) > maxHeight) {
            constrainedHeightSpec = View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.EXACTLY)
        }

        super.onMeasure(constrainedWidthSpec, constrainedHeightSpec)
    }

    companion object {

        @Px
        private const val NO_CONSTRAINTS = Integer.MAX_VALUE
    }
}
