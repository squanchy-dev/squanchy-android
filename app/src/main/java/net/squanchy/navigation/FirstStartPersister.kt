package net.squanchy.navigation

import android.content.SharedPreferences

internal class FirstStartPersister(private val preferences: SharedPreferences) {

    fun hasBeenStartedAlready() = preferences.getBoolean(KEY_HAS_BEEN_STARTED_ALREADY, false)

    fun storeHasBeenStarted() {
        preferences.edit()
            .putBoolean(KEY_HAS_BEEN_STARTED_ALREADY, true)
            .apply()
    }

    companion object {

        private const val KEY_HAS_BEEN_STARTED_ALREADY = "FirstStart.has_been_started_already"
    }
}
