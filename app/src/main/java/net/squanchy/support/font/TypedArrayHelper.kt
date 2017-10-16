@file:JvmName("FontCompat")

package net.squanchy.support.font

import android.content.Context
import android.support.annotation.StyleRes
import android.support.v4.content.res.ResourcesCompat
import net.squanchy.R

fun Context.getFontFor(@StyleRes styleResId: Int) =
        ResourcesCompat.getFont(this, R.font.title_typeface)!!
