package net.squanchy.navigation

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
internal class RoutingModule {

    @Provides
    fun firstStartPersister(activity: Activity): FirstStartPersister {
        // The preferences cannot be @Provide'd because we need different ones in different places
        // (but they are all SharedPreferences) and the Persister is our abstraction of preferences.
        val preferences = activity.getSharedPreferences(FIRST_START_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return FirstStartPersister(preferences)
    }

    companion object {

        private const val FIRST_START_SHARED_PREFERENCES_NAME = "first_start"
    }
}
