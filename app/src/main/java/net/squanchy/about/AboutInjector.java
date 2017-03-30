package net.squanchy.about;

import android.app.Activity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.navigation.NavigationModule;

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
