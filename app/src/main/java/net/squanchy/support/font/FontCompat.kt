@file:JvmName("FontCompat")

package net.squanchy.support.font

import android.content.Context
import android.graphics.Typeface
import android.support.annotation.StyleRes
import android.support.v4.content.res.ResourcesCompat
import net.squanchy.R

fun Context.getFontFor(@StyleRes styleResId: Int): Typeface {
    val typeface: Typeface
    val typedArray = obtainStyledAttributes(styleResId, kotlin.intArrayOf(R.attr.fontFamily))
    if (typedArray.hasValue(0)) {
        val fontId = typedArray.getResourceId(0, -1)
        typeface = ResourcesCompat.getFont(this, fontId)!!
    } else {
        error("TypedArray does not contain any fontFamily attribute!")
    }
    typedArray.recycle()
    return typeface
}
