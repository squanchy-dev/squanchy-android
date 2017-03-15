package net.squanchy.schedule;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;
import net.squanchy.service.repository.EventRepository;

import dagger.Module;
import dagger.Provides;

@Module
class ScheduleModule {

    @Provides
    ScheduleService scheduleService(@DbServiceType(DbServiceType.Type.AUTHENTICATED) FirebaseDbService dbService, EventRepository eventRepository) {
        return new ScheduleService(dbService, eventRepository);
    }
}
