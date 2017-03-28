package net.squanchy.settings;

import android.app.Activity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.signin.SignInModule;

final class SettingsInjector {

    private SettingsInjector() {
        // no instances
    }

    public static SettingsComponent obtain(Activity activity) {
        return DaggerSettingsComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .applicationComponent(ApplicationInjector.obtain(activity))
                .navigationModule(new NavigationModule())
                .signInModule(new SignInModule())
                .build();
    }
}
