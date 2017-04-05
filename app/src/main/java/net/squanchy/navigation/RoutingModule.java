package net.squanchy.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import net.squanchy.injection.ActivityContextModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = ActivityContextModule.class)
class RoutingModule {

    private static final String FIRST_START_SHARED_PREFERENCES_NAME = "first_start";

    @Provides
    FirstStartPersister firstStartPersister(Activity activity) {
        // The preferences cannot be @Provide'd because we need different ones in different places
        // (but they are all SharedPreferences) and the Persister is our abstraction of preferences.
        SharedPreferences preferences = activity.getSharedPreferences(FIRST_START_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new FirstStartPersister(preferences);
    }
}
