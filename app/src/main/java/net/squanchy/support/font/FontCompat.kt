@file:JvmName("FontCompat")

package net.squanchy.support.font

import android.content.Context
import android.content.res.TypedArray
import android.support.annotation.FontRes
import android.support.annotation.StyleRes
import android.support.v4.content.res.ResourcesCompat
import net.squanchy.R

fun Context.getFontFor(@StyleRes styleResId: Int) =
        ResourcesCompat.getFont(this, getFontIdFrom(styleResId))!!

@FontRes
private fun Context.getFontIdFrom(@StyleRes styleResId: Int) =
        obtainStyledAttributes(styleResId, kotlin.intArrayOf(R.attr.fontFamily)).use {
            if (hasValue(0)) {
                getResourceId(0, -1)
            } else {
                error("TypedArray does not contain any fontFamily attribute!")
            }
        }

inline fun <R> TypedArray.use(block: TypedArray.() -> R): R {
    try {
        return block()
    } finally {
        recycle()
    }
}
