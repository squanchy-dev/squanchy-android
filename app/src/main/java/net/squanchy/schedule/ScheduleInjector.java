package net.squanchy.schedule;

import android.app.Activity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.NavigationModule;

public final class ScheduleInjector {

    private ScheduleInjector() {
        // no instances
    }

    public static ScheduleComponent obtain(Activity activity) {
        return DaggerScheduleComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .scheduleModule(new ScheduleModule())
                .navigationModule(new NavigationModule())
                .activityContextModule(new ActivityContextModule(activity))
                .build();
    }
}
