package net.squanchy.schedule;

import net.squanchy.injection.ApplicationInjector;

final class ScheduleInjector {

    private ScheduleInjector() {
        // no instances
    }

    public static ScheduleComponent obtain(ScheduleActivity activity) {
        return DaggerScheduleComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .scheduleModule(new ScheduleModule(activity))
                .build();
    }
}
