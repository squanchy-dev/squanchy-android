package net.squanchy.about;

import android.support.v7.app.AppCompatActivity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.navigation.NavigationModule;

final class AboutInjector {

    private AboutInjector() {
        // no instances
    }

    public static AboutComponent obtain(AppCompatActivity activity) {
        return DaggerAboutComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .navigationModule(new NavigationModule())
                .build();
    }
}
