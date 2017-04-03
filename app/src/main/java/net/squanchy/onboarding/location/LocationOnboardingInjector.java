package net.squanchy.onboarding.location;

import android.app.Activity;

import com.google.android.gms.common.api.GoogleApiClient;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.proximity.preconditions.PreconditionsRegistryModule;
import net.squanchy.proximity.preconditions.ProximityPreconditions;
import net.squanchy.proximity.preconditions.ProximityPreconditionsModule;
import net.squanchy.proximity.preconditions.TaskLauncher;

final class LocationOnboardingInjector {

    private LocationOnboardingInjector() {
        // no instances
    }

    public static LocationOnboardingComponent obtain(
            Activity activity,
            TaskLauncher taskLauncher,
            GoogleApiClient googleApiClient,
            ProximityPreconditions.Callback callback
    ) {
        return DaggerLocationOnboardingComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .applicationComponent(ApplicationInjector.obtain(activity))
                .preconditionsRegistryModule(new PreconditionsRegistryModule(googleApiClient, taskLauncher))
                .proximityPreconditionsModule(new ProximityPreconditionsModule(callback))
                .build();
    }
}
