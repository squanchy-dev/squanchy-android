package net.squanchy.settings;

import android.app.Activity;

import com.google.android.gms.common.api.GoogleApiClient;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.proximity.preconditions.OptInPreferencePersisterModule;
import net.squanchy.proximity.preconditions.PreconditionsRegistryModule;
import net.squanchy.proximity.preconditions.ProximityPreconditions;
import net.squanchy.proximity.preconditions.ProximityPreconditionsModule;
import net.squanchy.proximity.preconditions.TaskLauncher;
import net.squanchy.signin.SignInModule;

final class SettingsInjector {

    private SettingsInjector() {
        // no instances
    }

    static SettingsFragmentComponent obtainForFragment(
            Activity activity,
            TaskLauncher taskLauncher,
            GoogleApiClient googleApiClient,
            ProximityPreconditions.Callback callback
    ) {
        return DaggerSettingsFragmentComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .applicationComponent(ApplicationInjector.obtain(activity))
                .navigationModule(new NavigationModule())
                .signInModule(new SignInModule())
                .optInPreferencePersisterModule(new OptInPreferencePersisterModule())
                .preconditionsRegistryModule(new PreconditionsRegistryModule(googleApiClient, taskLauncher))
                .proximityPreconditionsModule(new ProximityPreconditionsModule(callback))
                .applicationContextModule(new ApplicationContextModule(activity.getApplication()))      // This shouldn't be necessary
                .build();
    }

    static SettingsActivityComponent obtainForFragment(Activity activity) {
        return DaggerSettingsActivityComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .signInModule(new SignInModule())
                .build();
    }
}
