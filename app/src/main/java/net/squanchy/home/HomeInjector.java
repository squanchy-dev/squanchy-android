package net.squanchy.home;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.proximity.preconditions.PreconditionsRegistryModule;
import net.squanchy.proximity.preconditions.ProximityPreconditions;
import net.squanchy.proximity.preconditions.ProximityPreconditionsModule;
import net.squanchy.proximity.preconditions.TaskLauncherActivityModule;

final class HomeInjector {

    private HomeInjector() {
        // no instances
    }

    public static HomeComponent obtain(
            AppCompatActivity activity,
            GoogleApiClient googleApiClient,
            ProximityPreconditions.Callback callback
    ) {
        return DaggerHomeComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .activityContextModule(new ActivityContextModule(activity))
                .proximityPreconditionsModule(new ProximityPreconditionsModule(callback))
                .preconditionsRegistryModule(new PreconditionsRegistryModule(googleApiClient))
                .taskLauncherActivityModule(new TaskLauncherActivityModule())
                .build();
    }
}
