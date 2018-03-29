package net.squanchy.eventdetails

import dagger.Module
import dagger.Provides
import net.squanchy.service.repository.AuthService
import net.squanchy.service.repository.EventRepository

@Module
internal class EventDetailsModule {

    @Provides
    fun eventDetailsService(eventRepository: EventRepository, authService: AuthService): EventDetailsService {
        return EventDetailsService(eventRepository, authService)
    }
}
