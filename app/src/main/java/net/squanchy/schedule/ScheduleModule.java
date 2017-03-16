package net.squanchy.schedule;

import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.repository.EventRepository;

import dagger.Module;
import dagger.Provides;

@Module
class ScheduleModule {

    @Provides
    ScheduleService scheduleService(
            FirebaseDbService dbService,
            FirebaseAuthService authService,
            EventRepository eventRepository
    ) {
        return new ScheduleService(dbService, authService, eventRepository);
    }
}
