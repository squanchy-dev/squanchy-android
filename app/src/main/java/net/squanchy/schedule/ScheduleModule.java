package net.squanchy.schedule;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;

import dagger.Module;
import dagger.Provides;

@Module
class ScheduleModule {

    ScheduleModule() {

    }

    @Provides
    ScheduleService scheduleService(@DbServiceType(DbServiceType.Type.AUTHENTICATED) FirebaseDbService dbService) {
        return new ScheduleService(dbService);
    }
}
