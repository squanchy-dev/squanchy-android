package net.squanchy.eventdetails;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;
import net.squanchy.support.lang.Checksum;

import dagger.Module;
import dagger.Provides;

@Module
class EventDetailsModule {
    
    @Provides
    EventDetailsService scheduleService(@DbServiceType(DbServiceType.Type.AUTHENTICATED) FirebaseDbService dbService, Checksum checksum) {
        return new EventDetailsService(dbService, checksum);
    }
}
