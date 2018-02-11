package net.squanchy.service.repository.firebase

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import net.squanchy.schedule.domain.view.Event
import net.squanchy.service.firebase.FirebaseDbService
import net.squanchy.service.firebase.model.FirebaseFavorites
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firestore.model.schedule.FirestoreEvent
import net.squanchy.service.firestore.toEvent
import net.squanchy.service.repository.EventRepository
import net.squanchy.support.lang.Checksum
import org.joda.time.DateTimeZone

class FirebaseEventRepository(
    private val dbService: FirebaseDbService,
    private val firestoreDbService: FirestoreDbService,
    private val checksum: Checksum
) : EventRepository {

    override fun event(eventId: String, userId: String): Observable<Event> {
        val eventObservable = firestoreDbService.event(eventId)
        val favoritesObservable = dbService.favorites(userId)
        val timeZoneObservable = firestoreDbService.venueInfo().map { it.extractTimeZone() }

        return Observable.combineLatest(
            eventObservable,
            favoritesObservable,
            timeZoneObservable,
            Function3(combineIntoEvent)
        ).subscribeOn(Schedulers.io())
    }

    private fun FirestoreVenue.extractTimeZone(): DateTimeZone {
        return DateTimeZone.forID(timezone)
    }

    private val combineIntoEvent: (FirestoreEvent, FirebaseFavorites, DateTimeZone) -> Event
        get() = { event, favourites, timezone -> event.toEvent(checksum, timezone, favourites) }

    private val combineIntoEvents: (List<FirestoreEvent>, FirebaseFavorites, DateTimeZone) -> List<Event>
        get() = { event, favourites, timezone -> event.map { it.toEvent(checksum, timezone, favourites) } }

    override fun events(userId: String): Observable<List<Event>> {
        val sessionsObservable = firestoreDbService.events()
        val favoritesObservable = dbService.favorites(userId)
        val timeZoneObservable = firestoreDbService.venueInfo().map { it.extractTimeZone() }

        return Observable.combineLatest(
            sessionsObservable,
            favoritesObservable,
            timeZoneObservable,
            Function3(combineIntoEvents)
        )
    }

    override fun addFavorite(eventId: String, userId: String): Completable {
        return dbService.addFavorite(eventId, userId)
    }

    override fun removeFavorite(eventId: String, userId: String): Completable {
        return dbService.removeFavorite(eventId, userId)
    }
}
