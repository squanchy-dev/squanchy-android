package net.squanchy.support.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Outline
import android.support.annotation.Px
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout

import net.squanchy.R

open class CardLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = R.attr.cardViewDefaultStyle,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        super.setClipToOutline(false)

        val a = context.obtainStyledAttributes(attrs, R.styleable.CardLayout, defStyleAttr, defStyleRes)
        val insetHorizontal: Int
        val insetTop: Int
        val insetBottom: Int
        val radius: Int
        try {
            ensureHasNecessaryAttributes(a)
            insetHorizontal = a.getDimensionPixelSize(R.styleable.CardLayout_cardInsetHorizontal, 0)
            insetTop = a.getDimensionPixelSize(R.styleable.CardLayout_cardInsetTop, 0)
            insetBottom = a.getDimensionPixelSize(R.styleable.CardLayout_cardInsetBottom, 0)
            radius = a.getDimensionPixelSize(R.styleable.CardLayout_cardCornerRadius, 0)
        } finally {
            a.recycle()
        }

        applyInsetsAndRadius(insetHorizontal, insetTop, insetBottom, radius)
    }

    private fun ensureHasNecessaryAttributes(a: TypedArray) {
        if (!a.hasValue(R.styleable.CardLayout_cardInsetHorizontal)) {
            error("Missing the cardInsetHorizontal attribute in the style")
        }
        if (!a.hasValue(R.styleable.CardLayout_cardInsetTop)) {
            error("Missing the cardInsetTop attribute in the style")
        }
        if (!a.hasValue(R.styleable.CardLayout_cardInsetBottom)) {
            error("Missing the cardInsetBottom attribute in the style")
        }
        if (!a.hasValue(R.styleable.CardLayout_cardCornerRadius)) {
            error("Missing the cardCornerRadius attribute in the style")
        }
    }

    private fun applyInsetsAndRadius(insetHorizontal: Int, insetTop: Int, insetBottom: Int, radius: Int) {
        super.setOutlineProvider(
                NarrowerOutlineProvider(insetHorizontal, insetTop, insetBottom, radius)
        )
    }

    override fun setOutlineProvider(provider: ViewOutlineProvider) {
        throw UnsupportedOperationException("Cannot set an outline provider on a CardLayout")
    }

    override fun setClipToOutline(clipToOutline: Boolean) {
        throw UnsupportedOperationException("Cannot set clipping to outline on a CardLayout")
    }

    private class NarrowerOutlineProvider(
            @param:Px @field:Px
            private val insetHorizontal: Int,
            @param:Px @field:Px
            private val insetTop: Int,
            @param:Px @field:Px
            private val insetBottom: Int,
            @param:Px @field:Px
            private val radius: Int
    ) : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(
                    insetHorizontal,
                    insetTop,
                    view.width - insetHorizontal,
                    view.height - insetBottom,
                    radius.toFloat()
            )
        }
    }
}
