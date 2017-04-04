package net.squanchy.schedule;

import android.support.v7.app.AppCompatActivity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.NavigationModule;

final class ScheduleInjector {

    private ScheduleInjector() {
        // no instances
    }

    public static ScheduleComponent obtain(AppCompatActivity activity) {
        return DaggerScheduleComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .scheduleModule(new ScheduleModule())
                .navigationModule(new NavigationModule())
                .activityContextModule(new ActivityContextModule(activity))
                .build();
    }
}
