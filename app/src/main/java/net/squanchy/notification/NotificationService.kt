package net.squanchy.notification

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import net.squanchy.schedule.domain.view.Event
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.repository.EventRepository
import java.util.Collections
import java.util.Comparator

internal class NotificationService(private val authService: FirebaseAuthService, private val eventRepository: EventRepository) {

    fun sortedFavourites(): Observable<List<Event>> {
        return authService.ifUserSignedInThenObservableFrom { userId ->
            eventRepository.events(userId)
                    .map { events -> events.filter { it.favorited } }
                    .map { events ->
                        Collections.sort(events, byStartDate())
                        events
                    }
                    .take(1)
                    .subscribeOn(Schedulers.io())
        }
    }

    private fun byStartDate(): Comparator<Event> {
        return kotlin.Comparator { event, otherEvent -> event.startTime.compareTo(otherEvent.startTime) }
    }
}
