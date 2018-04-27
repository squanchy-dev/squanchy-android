package net.squanchy.service.repository.firestore

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function3
import net.squanchy.schedule.domain.view.Event
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.firebase.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firebase.model.schedule.FirestoreEvent
import net.squanchy.service.firebase.model.schedule.FirestoreFavorite
import net.squanchy.service.firebase.toEvent
import net.squanchy.service.repository.EventRepository
import net.squanchy.support.checksum.Checksum
import org.joda.time.DateTimeZone

class FirestoreEventRepository(
    private val firestoreDbService: FirestoreDbService,
    private val checksum: Checksum
) : EventRepository {

    override fun event(eventId: String, userId: String): Observable<Event> {
        val eventObservable = firestoreDbService.event(eventId)
        val timeZoneObservable = firestoreDbService.venueInfo().map { it.extractTimeZone() }
        val favoritesObservable = firestoreDbService.favorites(userId).map { it.map { it.id } }

        return Observable.combineLatest(
            eventObservable,
            timeZoneObservable,
            favoritesObservable,
            Function3(::combineIntoEvent)
        )
    }

    private fun FirestoreVenue.extractTimeZone() = DateTimeZone.forID(timezone)

    override fun events(userId: String): Observable<List<Event>> {
        val sessionsObservable = firestoreDbService.events()
        val timeZoneObservable = firestoreDbService.venueInfo().map { it.extractTimeZone() }
        val favoritesObservable = firestoreDbService.favorites(userId)

        return Observable.combineLatest(
            sessionsObservable,
            timeZoneObservable,
            favoritesObservable,
            Function3(::combineIntoEvents)
        )
    }

    private fun combineIntoEvents(events: List<FirestoreEvent>, timeZone: DateTimeZone, favorites: List<FirestoreFavorite>): List<Event> =
        events.map { combineIntoEvent(it, timeZone, favorites.map { it.id }) }

    private fun combineIntoEvent(event: FirestoreEvent, timeZone: DateTimeZone, favoriteIds: List<String>): Event =
        event.toEvent(checksum, timeZone, favoriteIds.contains(event.id))

    override fun addFavorite(eventId: String, userId: String): Completable = firestoreDbService.addFavorite(eventId, userId)

    override fun removeFavorite(eventId: String, userId: String): Completable = firestoreDbService.removeFavorite(eventId, userId)
}
