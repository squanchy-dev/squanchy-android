package net.squanchy.onboarding

import android.content.SharedPreferences

import java.util.Locale

class OnboardingPersister(private val preferences: SharedPreferences) {

    internal fun savePageSeen(page: OnboardingPage) =
        preferences.edit()
            .putBoolean(preferenceKeyFor(page), true)
            .apply()

    internal fun pageSeen(page: OnboardingPage) = preferences.getBoolean(preferenceKeyFor(page), false)

    private fun preferenceKeyFor(page: OnboardingPage) = String.format("key_onboarding_%s_seen", page.name.toLowerCase(Locale.US))
}
