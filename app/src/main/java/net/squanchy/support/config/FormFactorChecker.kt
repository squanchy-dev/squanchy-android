package net.squanchy.support.config

import android.content.Context

import net.squanchy.R

internal class FormFactorChecker(private val context: Context) {

    val isTablet: Boolean
        get() = context.resources
            .getBoolean(R.bool.isTablet)
}
