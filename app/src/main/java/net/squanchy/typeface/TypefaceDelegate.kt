package net.squanchy.typeface

import android.graphics.Paint
import android.graphics.Typeface
import net.squanchy.extensions.whenTrue

class TypefaceDelegate(private val newTypeface: Typeface) {

    private companion object {
        @SuppressWarnings("MagicNumber")
        const val TEXT_SKEW_X = -.25f
        const val FALSE_FLAG = 0
    }

    fun applyStyleTo(paint: Paint) {
        val fakeStyle = fakeStyle(paint)
        bold(fakeStyle).whenTrue { paint.isFakeBoldText = true }
        italic(fakeStyle).whenTrue { paint.textSkewX = TEXT_SKEW_X }

        paint.typeface = newTypeface
    }

    private fun bold(fakeStyle: Int) = fakeStyle.and(Typeface.BOLD) != FALSE_FLAG

    private fun italic(fakeStyle: Int) = fakeStyle.and(Typeface.ITALIC) != FALSE_FLAG

    private fun fakeStyle(paint: Paint) : Int {
        val oldStyle = paint.typeface?.style ?:0
        return oldStyle.and(newTypeface.style.inv())
    }
}
