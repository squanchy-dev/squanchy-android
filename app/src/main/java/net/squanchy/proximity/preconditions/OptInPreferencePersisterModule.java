package net.squanchy.proximity.preconditions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import net.squanchy.injection.ActivityContextModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = ActivityContextModule.class)
public class OptInPreferencePersisterModule {

    private static final String OPT_IN_PREFERENCES_NAME = "opt_in_preferences";

    @Provides
    OptInPreferencePersister optInPreferencePersister(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(OPT_IN_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new OptInPreferencePersister(preferences);
    }
}
