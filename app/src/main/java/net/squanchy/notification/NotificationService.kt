package net.squanchy.notification

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import net.squanchy.schedule.domain.view.Event
import net.squanchy.service.repository.AuthService
import net.squanchy.service.repository.EventRepository

internal class NotificationService(private val authService: AuthService, private val eventRepository: EventRepository) {

    fun sortedFavourites(): Single<List<Event>> {
        return authService.ifUserSignedInThenObservableFrom { userId ->
            eventRepository.events(userId)
                .map { it.filter { it.favorite } }
                .map { it.sortedBy { it.startTime } }
                .subscribeOn(Schedulers.io())
        }.firstOrError()
    }
}
