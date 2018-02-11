package net.squanchy.service.repository.firebase

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firebase.FirebaseDbService
import net.squanchy.service.firebase.model.FirebaseFavorites
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firestore.model.schedule.FirestoreEvent
import net.squanchy.service.firestore.model.schedule.FirestorePlace
import net.squanchy.service.firestore.model.schedule.FirestoreSpeaker
import net.squanchy.service.firestore.model.schedule.FirestoreTrack
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.lang.Checksum
import net.squanchy.support.lang.Optional
import net.squanchy.support.lang.optional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime

class FirebaseEventRepository(
    private val dbService: FirebaseDbService,
    private val firestoreDbService: FirestoreDbService,
    private val checksum: Checksum,
    private val speakerRepository: SpeakerRepository
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

    private fun FirestorePlace.toPlace(): Place = Place.create(id, name, floor.optional())

    private fun FirestoreTrack.toTrack() = Track.create(
        id,
        name,
        accentColor.optional(),
        textColor.optional(),
        iconUrl.optional()
    )

    private val combineIntoEvent: (FirestoreEvent, FirebaseFavorites, DateTimeZone) -> Event
        get() = { apiEvent, favorites, timeZone ->
            Event.create(
                apiEvent.id,
                checksum.getChecksumOf(apiEvent.id),
                LocalDateTime(apiEvent.startTime),
                LocalDateTime(apiEvent.endTime),
                apiEvent.title,
                apiEvent.place?.toPlace().optional(),
                Optional.fromNullable(apiEvent.experienceLevel).flatMap { ExperienceLevel.tryParsingFrom(it) },
                apiEvent.speakers.map { it.toSpeaker() },
                Event.Type.fromRawType(apiEvent.type),
                favorites.hasFavorite(apiEvent.id),
                Optional.fromNullable(apiEvent.description),
                apiEvent.track?.toTrack().optional(),
                timeZone
            )
        }

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

    private val combineIntoEvents: (List<FirestoreEvent>, FirebaseFavorites, DateTimeZone) -> List<Event>
        get() = { firebaseEvents, favorites, timeZone -> firebaseEvents.map(combineEventWith(favorites, timeZone)) }

    private fun combineEventWith(favorites: FirebaseFavorites, timeZone: DateTimeZone): ((FirestoreEvent) -> Event) {
        return { apiEvent ->
            Event.create(
                apiEvent.id,
                checksum.getChecksumOf(apiEvent.id),
                LocalDateTime(apiEvent.startTime),
                LocalDateTime(apiEvent.endTime),
                apiEvent.title,
                apiEvent.place?.toPlace().optional(),
                Optional.fromNullable(apiEvent.experienceLevel).flatMap { ExperienceLevel.tryParsingFrom(it) },
                apiEvent.speakers.map { it.toSpeaker() },
                Event.Type.fromRawType(apiEvent.type),
                favorites.hasFavorite(apiEvent.id),
                Optional.fromNullable(apiEvent.description),
                apiEvent.track?.toTrack().optional(),
                timeZone
            )
        }
    }

    private fun FirestoreSpeaker.toSpeaker() = Speaker(
        numericId = checksum.getChecksumOf(id),
        id = id,
        name = name,
        bio = bio,
        companyName = companyName.optional(),
        companyUrl = companyUrl.optional(),
        personalUrl = personalUrl.optional(),
        photoUrl = photoUrl.optional(),
        twitterUsername = twitterUsername.optional()
    )

    override fun addFavorite(eventId: String, userId: String): Completable {
        return dbService.addFavorite(eventId, userId)
    }

    override fun removeFavorite(eventId: String, userId: String): Completable {
        return dbService.removeFavorite(eventId, userId)
    }
}
