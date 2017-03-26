package net.squanchy.navigation;

import android.app.Activity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.deeplink.DeepLinkModule;
import net.squanchy.signin.SignInModule;

final class RoutingInjector {

    private RoutingInjector() {
        // no instances
    }

    public static RoutingComponent obtain(Activity activity) {
        return DaggerRoutingComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .applicationComponent(ApplicationInjector.obtain(activity))
                .deepLinkModule(new DeepLinkModule())
                .navigationModule(new NavigationModule())
                .signInModule(new SignInModule())
                .build();
    }
}
