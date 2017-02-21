package net.squanchy.schedule;

import android.content.Context;

import net.squanchy.injection.ApplicationInjector;

public final class ScheduleInjector {

    private ScheduleInjector() {
        // no instances
    }

    public static ScheduleComponent obtain(Context context) {
        return DaggerScheduleComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(context))
                .scheduleModule(new ScheduleModule())
                .build();
    }
}
