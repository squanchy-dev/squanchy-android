package net.squanchy.injection;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityContextModule {

    private final Activity activity;

    public ActivityContextModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity activityContext() {
        return activity;
    }
}
