package net.squanchy.eventdetails

import net.squanchy.service.firestore.FirebaseAuthService

import dagger.Module
import dagger.Provides
import net.squanchy.service.repository.firestore.EventRepository

@Module
internal class EventDetailsModule {

    @Provides
    fun eventDetailsService(eventRepository: EventRepository, authService: FirebaseAuthService): EventDetailsService {
        return EventDetailsService(eventRepository, authService)
    }
}
