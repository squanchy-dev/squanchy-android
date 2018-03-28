package net.squanchy.support.graphics

import android.support.annotation.ColorInt
import android.support.v4.graphics.ColorUtils

@ColorInt
internal fun Int.pickBestTextColorByContrast(@ColorInt firstTextColor: Int, @ColorInt secondTextColor: Int): Int {
    val firstTextContrast = ColorUtils.calculateContrast(firstTextColor, this)
    val secondTextContrast = ColorUtils.calculateContrast(secondTextColor, this)

    return when {
        firstTextContrast > secondTextContrast -> firstTextColor
        else -> secondTextColor
    }
}
