package net.squanchy.analytics

import android.content.SharedPreferences

internal class FirstStartUserPropertiesPersister(private val preferences: SharedPreferences) {

    fun userPropertiesSetAlready() = preferences.getBoolean(KEY_USER_PROPERTIES_SET_ALREADY, false)

    fun storeUserPropertiesSet() {
        preferences.edit()
            .putBoolean(KEY_USER_PROPERTIES_SET_ALREADY, true)
            .apply()
    }

    companion object {

        private const val KEY_USER_PROPERTIES_SET_ALREADY = "FirstStart.has_set_default_user_properties_already"
    }
}
