package com.connfa.schedule;

import com.connfa.service.firebase.FirebaseConnfaRepository;

import dagger.Module;
import dagger.Provides;

@Module
class ScheduleModule {

    private final ScheduleActivity activity;

    ScheduleModule(ScheduleActivity activity) {
        this.activity = activity;
    }

    @Provides
    ScheduleService scheduleService(FirebaseConnfaRepository repository) {
        return new ScheduleService(repository);
    }

    @Provides
    ScheduleNavigator scheduleNavigator() {
        return new ScheduleNavigator(activity);
    }
}
