package net.squanchy.support.graphics

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.ColorUtils.HSLToColor
import androidx.core.graphics.ColorUtils.calculateContrast
import androidx.core.graphics.ColorUtils.colorToHSL
import androidx.core.graphics.ColorUtils.compositeColors

@ColorInt
internal fun Int.pickBestTextColorByContrast(@ColorInt firstTextColor: Int, @ColorInt secondTextColor: Int): Int {
    val firstTextContrast = ColorUtils.calculateContrast(firstTextColor, this)
    val secondTextContrast = ColorUtils.calculateContrast(secondTextColor, this)

    return when {
        firstTextContrast > secondTextContrast -> firstTextColor
        else -> secondTextColor
    }
}

@ColorInt
internal fun Int.darkenToEnsureTextContrasts(@ColorInt background: Int, @ColorInt fallbackTextColor: Int): Int {
    var currentColor = this
    var compositeColor = compositeColors(currentColor, background)
    var contrastRatio = calculateContrast(compositeColor, background)

    val hslColor: HSLColor = FloatArray(HSL_COMPONENTS)
    colorToHSL(compositeColor, hslColor)
    var numIterations = 0

    while (numIterations <= CONTRAST_SEARCH_MAX_ITERATIONS && hslColor.lightness >= MIN_LIGHTNESS) {
        if (contrastRatio >= MIN_CONTRAST_RATIO) {
            return currentColor
        }

        val newLightness = Math.max(hslColor.lightness - LIGHTNESS_STEP_SIZE, MIN_LIGHTNESS)
        hslColor.lightness = newLightness
        currentColor = HSLToColor(hslColor)
        compositeColor = compositeColors(currentColor, background)
        contrastRatio = calculateContrast(compositeColor, background)

        numIterations++
    }
    return fallbackTextColor
}

private const val HSL_COMPONENTS = 3

private const val CONTRAST_SEARCH_MAX_ITERATIONS = 10F
private const val LIGHTNESS_STEP_SIZE = .05F
private const val MIN_CONTRAST_RATIO = 3.5F
private const val MIN_LIGHTNESS = 0.0F
private const val MAX_LIGHTNESS = 1.0F

private typealias HSLColor = FloatArray
private const val HSL_COMPONENT_LIGHTNESS = 2

private var HSLColor.lightness
    @FloatRange(from = MIN_LIGHTNESS.toDouble(), to = MAX_LIGHTNESS.toDouble())
    get() = this[HSL_COMPONENT_LIGHTNESS]
    set(@FloatRange(from = MIN_LIGHTNESS.toDouble(), to = MAX_LIGHTNESS.toDouble()) value) {
        this[HSL_COMPONENT_LIGHTNESS] = value
    }

internal fun Int.toColorStateList() = ColorStateList.valueOf(this)
