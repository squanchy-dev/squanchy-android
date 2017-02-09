package net.squanchy.eventdetails;

import net.squanchy.service.firebase.FirebaseSquanchyRepository;

import dagger.Module;
import dagger.Provides;

@Module
class EventDetailsModule {
    
    @Provides
    EventDetailsService scheduleService(FirebaseSquanchyRepository repository) {
        return new EventDetailsService(repository);
    }
}
