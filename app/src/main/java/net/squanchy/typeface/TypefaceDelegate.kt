package net.squanchy.typeface

import android.graphics.Paint
import android.graphics.Typeface
import net.squanchy.extensions.whenTrue

class TypefaceDelegate(private val newTypeface: Typeface) {

    fun applyStyleTo(paint: Paint) {
        val fakeStyle = fakeStyle(paint)
        bold(fakeStyle).whenTrue { paint.isFakeBoldText = true }
        italic(fakeStyle).whenTrue { paint.textSkewX = -.25f }

        paint.typeface = newTypeface
    }

    private fun bold(fakeStyle: Int) = fakeStyle.and(Typeface.BOLD) != 0

    private fun italic(fakeStyle: Int) = fakeStyle.and(Typeface.ITALIC) != 0

    private fun fakeStyle(paint: Paint) : Int {
        val oldStyle = paint.typeface?.style ?:0
        return oldStyle.and(newTypeface.style.inv())
    }
}
