package net.squanchy.navigation;

import android.app.Activity;

import net.squanchy.analytics.AnalyticsModule;
import net.squanchy.injection.ActivityContextModule;

final class HomeInjector {

    private HomeInjector() {
        // no instances
    }

    public static HomeComponent obtain(Activity activity) {
        return DaggerHomeComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .analyticsModule(new AnalyticsModule())
                .navigationModule(new NavigationModule())
                .build();
    }
}
