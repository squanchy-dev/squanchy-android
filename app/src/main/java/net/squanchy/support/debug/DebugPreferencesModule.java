package net.squanchy.support.debug;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class DebugPreferencesModule {

    @Provides
    DebugPreferences debugPreferences(Application application) {
        return new DebugPreferences(application);
    }
}
