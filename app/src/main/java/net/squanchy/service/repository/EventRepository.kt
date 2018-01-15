package net.squanchy.service.repository

import io.reactivex.Completable
import io.reactivex.Observable
import net.squanchy.schedule.domain.view.Event

interface EventRepository {

    fun event(eventId: String, userId: String): Observable<Event>

    fun events(userId: String): Observable<List<Event>>

    fun addFavorite(eventId: String, userId: String): Completable

    fun removeFavorite(eventId: String, userId: String): Completable
}
