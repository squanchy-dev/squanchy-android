package net.squanchy.home;

import android.app.Activity;

import com.google.android.gms.common.api.GoogleApiClient;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.proximity.preconditions.PreconditionsRegistryModule;
import net.squanchy.proximity.preconditions.ProximityPreconditions;
import net.squanchy.proximity.preconditions.ProximityPreconditionsModule;

final class HomeInjector {

    private HomeInjector() {
        // no instances
    }

    public static HomeComponent obtain(Activity activity, GoogleApiClient googleApiClient, ProximityPreconditions.Callback callback) {
        return DaggerHomeComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .activityContextModule(new ActivityContextModule(activity))
                .proximityPreconditionsModule(new ProximityPreconditionsModule(callback))
                .preconditionsRegistryModule(new PreconditionsRegistryModule(googleApiClient))
                .build();
    }
}
