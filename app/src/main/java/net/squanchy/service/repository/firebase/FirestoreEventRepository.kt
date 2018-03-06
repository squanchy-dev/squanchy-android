package net.squanchy.service.repository.firebase

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import net.squanchy.schedule.domain.view.Event
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firestore.model.schedule.FirestoreEvent
import net.squanchy.service.firestore.model.schedule.FirestoreFavorite
import net.squanchy.service.firestore.toEvent
import net.squanchy.service.repository.EventRepository
import net.squanchy.support.lang.Checksum
import org.joda.time.DateTimeZone

class FirestoreEventRepository(
    private val firestoreDbService: FirestoreDbService,
    private val checksum: Checksum
) : EventRepository {

    override fun event(eventId: String, userId: String): Observable<Event> {
        val eventObservable = firestoreDbService.event(eventId)
        val timeZoneObservable = firestoreDbService.venueInfo().map { it.extractTimeZone() }
        val favoritesObservable = firestoreDbService.favorites(userId)

        return Observable.combineLatest(
            eventObservable,
            timeZoneObservable,
            favoritesObservable,
            Function3(::combineIntoEvent)
        ).subscribeOn(Schedulers.io())
    }

    private fun FirestoreVenue.extractTimeZone() = DateTimeZone.forID(timezone)

    private fun combineIntoEvent(event: FirestoreEvent, timeZone: DateTimeZone, favorites: List<FirestoreFavorite>): Event =
        event.toEvent(checksum, timeZone, favorites.includes(event))

    private fun combineIntoEvents(events: List<FirestoreEvent>, timeZone: DateTimeZone, favorites: List<FirestoreFavorite>): List<Event> =
        events.map { combineIntoEvent(it, timeZone, favorites) }

    private fun List<FirestoreFavorite>.includes(event: FirestoreEvent) =
        mapNotNull { it.id }.contains(event.id)

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

    override fun addFavorite(eventId: String, userId: String): Completable = firestoreDbService.addFavorite(eventId, userId)

    override fun removeFavorite(eventId: String, userId: String): Completable = firestoreDbService.removeFavorite(eventId, userId)
}
