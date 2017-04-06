package net.squanchy.injection;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityContextModule {

    private final AppCompatActivity activity;

    public ActivityContextModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    AppCompatActivity appCompatActivityContext() {
        return activity;
    }

    @Provides
    Activity activityContext() {
        return activity;
    }
}
