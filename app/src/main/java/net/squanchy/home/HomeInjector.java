package net.squanchy.home;

import android.app.Activity;

import net.squanchy.analytics.AnalyticsModule;
import net.squanchy.injection.ActivityContextModule;
import net.squanchy.navigation.DaggerHomeComponent;
import net.squanchy.navigation.HomeComponent;
import net.squanchy.navigation.NavigationModule;

public final class HomeInjector {

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
