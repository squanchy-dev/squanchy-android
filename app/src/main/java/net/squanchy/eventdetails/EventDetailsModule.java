package net.squanchy.eventdetails;

import net.squanchy.service.repository.EventRepository;

import dagger.Module;
import dagger.Provides;

@Module
class EventDetailsModule {
    
    @Provides
    EventDetailsService scheduleService(EventRepository eventRepository) {
        return new EventDetailsService(eventRepository);
    }
}
