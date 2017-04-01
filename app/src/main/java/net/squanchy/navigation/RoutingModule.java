package net.squanchy.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import net.squanchy.injection.ApplicationContextModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = ApplicationContextModule.class)
class RoutingModule {

    private static final String FIRST_START_SHARED_PREFERENCES_NAME = "first_start";

    @Provides
    SharedPreferences firstStartSharedPreferences(Activity activity) {
        return activity.getSharedPreferences(FIRST_START_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    FirstStartPersister firstStartPersister(SharedPreferences preferences) {
        return new FirstStartPersister(preferences);
    }
}
