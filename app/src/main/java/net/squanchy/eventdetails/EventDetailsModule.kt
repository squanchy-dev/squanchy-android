package net.squanchy.eventdetails

import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.repository.EventRepository

import dagger.Module
import dagger.Provides

@Module
internal class EventDetailsModule {

    @Provides
    fun eventDetailsService(eventRepository: EventRepository, authService: FirebaseAuthService): EventDetailsService {
        return EventDetailsService(eventRepository, authService)
    }
}
