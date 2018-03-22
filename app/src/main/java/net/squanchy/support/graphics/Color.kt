package net.squanchy.support.graphics

import android.graphics.Color
import android.support.annotation.ColorInt

private const val MAX_COLOR_COMPONENT = 255.0
private const val GAMMA_FACTOR = 2.2
private const val MAX_LIGHTNESS_FOR_LIGHT_TEXT = .18
private const val FACTOR_RED = 0.2126
private const val FACTOR_GREEN = 0.7151
private const val FACTOR_BLUE = 0.0721

@ColorInt
internal fun Int.contrastingTextColor(darkTextColor: Int, lightTextColor: Int): Int {
    val r = Color.red(this)
    val g = Color.green(this)
    val b = Color.blue(this)
    val lightness = FACTOR_RED * gamaCorrectColorComponent(r) +
        FACTOR_GREEN * gamaCorrectColorComponent(g) +
        FACTOR_BLUE * gamaCorrectColorComponent(b)

    return if (lightness > MAX_LIGHTNESS_FOR_LIGHT_TEXT) {
        darkTextColor
    } else {
        lightTextColor
    }
}

private fun gamaCorrectColorComponent(r: Int) = Math.pow(r / MAX_COLOR_COMPONENT, GAMMA_FACTOR)
