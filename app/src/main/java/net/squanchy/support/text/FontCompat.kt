@file:JvmName("FontCompat")

package net.squanchy.support.text

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.FontRes
import androidx.annotation.StyleRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.use
import net.squanchy.R

fun Context.getFontFor(@StyleRes styleResId: Int) =
    ResourcesCompat.getFont(this, getFontIdFrom(styleResId))!!

@SuppressLint("Recycle") // The KTX TypedArray.use() takes care of it
@FontRes
private fun Context.getFontIdFrom(@StyleRes styleResId: Int) =
    obtainStyledAttributes(styleResId, kotlin.intArrayOf(R.attr.fontFamily)).use {
        if (it.hasValue(0)) {
            it.getResourceId(0, -1)
        } else {
            error("TypedArray does not contain any fontFamily attribute!")
        }
    }
