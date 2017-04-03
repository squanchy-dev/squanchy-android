package net.squanchy.settings;

import android.app.Activity;

import com.google.android.gms.common.api.GoogleApiClient;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.proximity.preconditions.OptInPreferencePersisterModule;
import net.squanchy.proximity.preconditions.PreconditionsRegistryModule;
import net.squanchy.proximity.preconditions.ProximityPreconditions;
import net.squanchy.proximity.preconditions.ProximityPreconditionsModule;
import net.squanchy.signin.SignInModule;

final class SettingsInjector {

    private SettingsInjector() {
        // no instances
    }

    static SettingsFragmentComponent obtainForFragment(Activity activity, GoogleApiClient googleApiClient, ProximityPreconditions.Callback callback) {
        return DaggerSettingsFragmentComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .applicationComponent(ApplicationInjector.obtain(activity))
                .navigationModule(new NavigationModule())
                .signInModule(new SignInModule())
                .optInPreferencePersisterModule(new OptInPreferencePersisterModule())
                .preconditionsRegistryModule(new PreconditionsRegistryModule(googleApiClient))
                .proximityPreconditionsModule(new ProximityPreconditionsModule(callback))
                .build();
    }

    static SettingsActivityComponent obtainForFragment(Activity activity) {
        return DaggerSettingsActivityComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .signInModule(new SignInModule())
                .build();
    }
}
