package net.squanchy.onboarding.location;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.proximity.preconditions.PreconditionsRegistryModule;
import net.squanchy.proximity.preconditions.ProximityPreconditions;
import net.squanchy.proximity.preconditions.ProximityPreconditionsModule;
import net.squanchy.proximity.preconditions.TaskLauncherActivityModule;
import net.squanchy.support.debug.DebugPreferencesModule;

final class LocationOnboardingInjector {

    private LocationOnboardingInjector() {
        // no instances
    }

    public static LocationOnboardingComponent obtain(
            AppCompatActivity activity,
            GoogleApiClient googleApiClient,
            ProximityPreconditions.Callback callback
    ) {
        return DaggerLocationOnboardingComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .applicationComponent(ApplicationInjector.obtain(activity))
                .preconditionsRegistryModule(new PreconditionsRegistryModule(googleApiClient))
                .proximityPreconditionsModule(new ProximityPreconditionsModule(callback))
                .debugPreferencesModule(new DebugPreferencesModule())
                .taskLauncherActivityModule(new TaskLauncherActivityModule())
                .build();
    }
}
