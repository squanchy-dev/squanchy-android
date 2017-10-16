package net.squanchy.support.font

import android.graphics.Paint
import android.graphics.Typeface

class TypefaceDelegate(private val newTypeface: Typeface) {

    private companion object {
        @SuppressWarnings("MagicNumber")
        const val TEXT_SKEW_X = -.25f
        const val FALSE_FLAG = 0
    }

    fun applyTypefaceTo(paint: Paint) {
        val previousStyle = computePreviousStyle(paint)

        if (isBold(previousStyle)) {
            paint.isFakeBoldText = true
        }
        if (isItalic(previousStyle)) {
            paint.textSkewX = TEXT_SKEW_X
        }

        paint.typeface = newTypeface
    }

    private fun isBold(fakeStyle: Int) = fakeStyle.and(Typeface.BOLD) != FALSE_FLAG

    private fun isItalic(fakeStyle: Int) = fakeStyle.and(Typeface.ITALIC) != FALSE_FLAG

    private fun computePreviousStyle(paint: Paint): Int {
        val oldStyle = paint.typeface?.style ?: 0
        return oldStyle.and(newTypeface.style.inv())
    }
}
