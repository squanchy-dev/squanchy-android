package net.squanchy.support.debug

import android.content.Context

internal class OnboardingResetter(context: Context) {

    companion object {

        private const val ONBOARDING_PREFERENCES_NAME = "onboarding"
    }

    private val preferences = context.getSharedPreferences(ONBOARDING_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun resetOnboarding() {
        preferences.edit()
                .clear()
                .apply()
    }
}
