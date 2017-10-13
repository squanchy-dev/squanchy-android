package net.squanchy.home;

import android.support.v7.app.AppCompatActivity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;

final class HomeInjector {

    private HomeInjector() {
        // no instances
    }

    public static HomeComponent obtain(
            AppCompatActivity activity
    ) {
        return DaggerHomeComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .activityContextModule(new ActivityContextModule(activity))
                .build();
    }
}
