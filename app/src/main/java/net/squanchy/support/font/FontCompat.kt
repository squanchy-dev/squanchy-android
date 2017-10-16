@file:JvmName("FontCompat")

package net.squanchy.support.font

import android.content.Context
import android.support.annotation.FontRes
import android.support.annotation.StyleRes
import android.support.v4.content.res.ResourcesCompat
import net.squanchy.R

fun Context.getFontFor(@StyleRes styleResId: Int) =
        ResourcesCompat.getFont(this, getFontIdFrom(styleResId))!!

@FontRes
private fun Context.getFontIdFrom(@StyleRes styleResId: Int): Int {
    @FontRes val fontId: Int

    val typedArray = obtainStyledAttributes(styleResId, kotlin.intArrayOf(R.attr.fontFamily))
    if (typedArray.hasValue(0)) {
        fontId = typedArray.getResourceId(0, -1)
    } else {
        error("TypedArray does not contain any fontFamily attribute!")
    }
    typedArray.recycle()
    return fontId
}
