package net.squanchy.navigation;

import android.app.Activity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.navigation.deeplink.DeepLinkModule;

final class RoutingInjector {

    private RoutingInjector() {
        // no instances
    }

    public static RoutingComponent obtain(Activity activity) {
        return DaggerRoutingComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .deepLinkModule(new DeepLinkModule())
                .navigationModule(new NavigationModule())
                .build();
    }
}
