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
    if (typedArray.hasValue(R.styleable.TextAppearance_android_fontFamily)) {
        val fontId = typedArray.getResourceId(R.styleable.TextAppearance_android_fontFamily, -1)
        typeface = ResourcesCompat.getFont(this, fontId)!!
    } else {
        error("")
    }
    typedArray.recycle()
    return typeface
}
