package net.squanchy.navigation;

import android.app.Activity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.deeplink.DeepLinkModule;
import net.squanchy.proximity.ProximityFeatureModule;
import net.squanchy.signin.SignInModule;
import net.squanchy.support.debug.DebugPreferencesModule;

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
                .routingModule(new RoutingModule())
                .proximityFeatureModule(new ProximityFeatureModule())
                .debugPreferencesModule(new DebugPreferencesModule())
                .applicationContextModule(new ApplicationContextModule(activity.getApplication()))      // This shouldn't be necessary
                .build();
    }
}
