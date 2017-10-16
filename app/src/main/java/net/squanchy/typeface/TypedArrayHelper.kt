package net.squanchy.typeface

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import net.squanchy.R

/**
 * This is a workaround for when Google exposes a proper way to get a font from resources without hitting private API
 * On Oreo, we could use TypedArray#getFont(int index), but I couldn't find the correct index for the theme and the documentation
 * does not help. Anyway, we should scan private attributes on versions prior to Oreo, in order to get the correct ID.
 *
 * Please check TypedArrayHelper#obtainFont for further information (this is where we should implement the real logic)
 */
class TypedArrayHelper(val context: Context) {
    val font : Typeface

    init {
        val typedArray = context.obtainStyledAttributes(R.style.TextAppearance_Squanchy_Tab, intArrayOf(R.attr.fontFamily))
        try {
            font = typedArray.obtainFont()
        } finally {
            typedArray.recycle()
        }
    }

    private fun TypedArray.obtainFont(): Typeface =
            ResourcesCompat.getFont(context, R.font.title_typeface)!!

}
