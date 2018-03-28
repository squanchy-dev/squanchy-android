package net.squanchy.analytics

import android.content.SharedPreferences

internal class FirstStartDetector(private val preferences: SharedPreferences) {

    fun isFirstStartNotLoggedInStatusTracked() = preferences.getBoolean(KEY_FIRST_START_NOT_LOGGED_IN_TRACKED, false)

    fun setFirstStartNotLoggedInStatusTracked() {
        preferences.edit()
            .putBoolean(KEY_FIRST_START_NOT_LOGGED_IN_TRACKED, true)
            .apply()
    }

    companion object {

        private const val KEY_FIRST_START_NOT_LOGGED_IN_TRACKED = "FirstStart.not_logged_in_tracked"
    }
}
