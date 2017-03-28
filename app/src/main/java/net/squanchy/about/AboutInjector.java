package net.squanchy.about;

import android.app.Activity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.settings.DaggerSettingsComponent;
import net.squanchy.settings.SettingsComponent;

final class AboutInjector {

    private AboutInjector() {
        // no instances
    }

    public static AboutComponent obtain(Activity activity) {
        return DaggerAboutComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .navigationModule(new NavigationModule())
                .build();
    }
}
