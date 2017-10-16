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
        val fakeStyle = fakeStyle(paint)

        if (bold(fakeStyle)) {
            paint.isFakeBoldText = true
        }
        if (italic(fakeStyle)) {
            paint.textSkewX = TEXT_SKEW_X
        }

        paint.typeface = newTypeface
    }

    private fun bold(fakeStyle: Int) = fakeStyle.and(Typeface.BOLD) != FALSE_FLAG

    private fun italic(fakeStyle: Int) = fakeStyle.and(Typeface.ITALIC) != FALSE_FLAG

    private fun fakeStyle(paint: Paint): Int {
        val oldStyle = paint.typeface?.style ?: 0
        return oldStyle.and(newTypeface.style.inv())
    }
}
