package net.squanchy.schedule;

import net.squanchy.service.firebase.FirebaseSquanchyRepository;

import dagger.Module;
import dagger.Provides;

@Module
class ScheduleModule {

    private final ScheduleActivity activity;

    ScheduleModule(ScheduleActivity activity) {
        this.activity = activity;
    }

    @Provides
    ScheduleService scheduleService(FirebaseSquanchyRepository repository) {
        return new ScheduleService(repository);
    }

    @Provides
    ScheduleNavigator scheduleNavigator() {
        return new ScheduleNavigator(activity);
    }
}
