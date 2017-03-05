package net.squanchy.schedule;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;
import net.squanchy.support.lang.Checksum;

import dagger.Module;
import dagger.Provides;

@Module
class ScheduleModule {

    @Provides
    ScheduleService scheduleService(@DbServiceType(DbServiceType.Type.AUTHENTICATED) FirebaseDbService dbService, Checksum checksum) {
        return new ScheduleService(dbService, checksum);
    }
}
