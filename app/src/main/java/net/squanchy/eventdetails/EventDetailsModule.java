package net.squanchy.eventdetails;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;

import dagger.Module;
import dagger.Provides;

@Module
class EventDetailsModule {
    
    @Provides
    EventDetailsService scheduleService(@DbServiceType(DbServiceType.Type.AUTHENTICATED) FirebaseDbService dbService) {
        return new EventDetailsService(dbService);
    }
}
