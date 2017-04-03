package net.squanchy.navigation;

import android.app.Activity;

import com.google.android.gms.common.api.GoogleApiClient;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.deeplink.DeepLinkModule;
import net.squanchy.proximity.preconditions.PreconditionsRegistryModule;
import net.squanchy.proximity.preconditions.ProximityPreconditions;
import net.squanchy.proximity.preconditions.ProximityPreconditionsModule;
import net.squanchy.proximity.preconditions.TaskLauncher;
import net.squanchy.signin.SignInModule;

final class RoutingInjector {

    private RoutingInjector() {
        // no instances
    }

    public static RoutingComponent obtain(
            Activity activity,
            TaskLauncher taskLauncher,
            GoogleApiClient googleApiClient,
            ProximityPreconditions.Callback callback
    ) {
        return DaggerRoutingComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .applicationComponent(ApplicationInjector.obtain(activity))
                .deepLinkModule(new DeepLinkModule())
                .navigationModule(new NavigationModule())
                .signInModule(new SignInModule())
                .routingModule(new RoutingModule())
                .preconditionsRegistryModule(new PreconditionsRegistryModule(googleApiClient, taskLauncher))
                .proximityPreconditionsModule(new ProximityPreconditionsModule(callback))
                .build();
    }
}
