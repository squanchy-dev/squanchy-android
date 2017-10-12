package net.squanchy.navigation;

import android.support.v7.app.AppCompatActivity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.deeplink.DeepLinkModule;
import net.squanchy.signin.SignInModule;
import net.squanchy.support.debug.DebugPreferencesModule;

final class RoutingInjector {

    private RoutingInjector() {
        // no instances
    }

    public static RoutingComponent obtain(AppCompatActivity activity) {
        return DaggerRoutingComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .applicationComponent(ApplicationInjector.obtain(activity))
                .deepLinkModule(new DeepLinkModule())
                .navigationModule(new NavigationModule())
                .signInModule(new SignInModule())
                .routingModule(new RoutingModule())
                .build();
    }
}
