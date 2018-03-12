package net.squanchy.notification

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import net.squanchy.schedule.domain.view.Event
import net.squanchy.service.firestore.FirebaseAuthService
import net.squanchy.service.repository.firestore.EventRepository

internal class NotificationService(private val authService: FirebaseAuthService, private val eventRepository: EventRepository) {

    fun sortedFavourites(): Observable<List<Event>> {
        return authService.ifUserSignedInThenObservableFrom { userId ->
            eventRepository.events(userId)
                    .map { it.filter { it.favorited } }
                    .map { it.sortedBy { it.startTime } }
                    .take(1)
                    .subscribeOn(Schedulers.io())
        }
    }
}
