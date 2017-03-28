package net.squanchy.home;

import android.app.Activity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;

final class HomeInjector {

    private HomeInjector() {
        // no instances
    }

    public static HomeComponent obtain(Activity activity) {
        return DaggerHomeComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .homeModule(new HomeModule())
                .activityContextModule(new ActivityContextModule(activity))
                .build();
    }
}
