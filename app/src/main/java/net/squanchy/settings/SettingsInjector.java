package net.squanchy.settings;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.signin.SignInModule;

final class SettingsInjector {

    private SettingsInjector() {
        // no instances
    }

    static SettingsFragmentComponent obtainForFragment(AppCompatActivity activity) {
        return DaggerSettingsFragmentComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .applicationComponent(ApplicationInjector.obtain(activity))
                .navigationModule(new NavigationModule())
                .signInModule(new SignInModule())
                .build();
    }

    static SettingsActivityComponent obtainForActivity(Activity activity) {
        return DaggerSettingsActivityComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .signInModule(new SignInModule())
                .build();
    }
}
