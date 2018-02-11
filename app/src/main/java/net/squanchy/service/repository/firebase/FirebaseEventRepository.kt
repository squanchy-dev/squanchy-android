package net.squanchy.service.repository.firebase

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.functions.Function6
import io.reactivex.schedulers.Schedulers
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firebase.FirebaseDbService
import net.squanchy.service.firebase.model.FirebaseFavorites
import net.squanchy.service.firebase.model.FirebasePlaces
import net.squanchy.service.firebase.model.FirebaseTracks
import net.squanchy.service.firebase.model.FirebaseVenue
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.schedule.FirestoreEvent
import net.squanchy.service.firestore.model.schedule.FirestorePlace
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
        val speakersObservable = speakerRepository.speakers()
        val favoritesObservable = dbService.favorites(userId)
        val placesObservable = dbService.places().map(toPlaces())                 // TODO access them by ID directly?
        val tracksObservable = dbService.tracks().map(toTracks)                 // TODO extract repositories?
        val timeZoneObservable = dbService.venueInfo().map(toTimeZone())

        return Observable.combineLatest(
                eventObservable,
                speakersObservable,
                favoritesObservable,
                placesObservable,
                tracksObservable,
                timeZoneObservable,
                Function6(combineIntoEvent)
        ).subscribeOn(Schedulers.io())
    }

    private fun toTimeZone(): Function<FirebaseVenue, DateTimeZone> {
        return Function { firebaseVenue -> DateTimeZone.forID(firebaseVenue.timezone) }
    }

    private fun toPlaces(): Function<FirebasePlaces, List<Place>> {
        return Function { firebasePlaces ->
            firebasePlaces.places!!.map {
                Place.create(
                        it.id!!,
                        it.name!!,
                        Optional.fromNullable(it.floor)
                )
            }
        }
    }

    private val toTracks: (FirebaseTracks) -> List<Track>
        get() = { firebaseTracks ->
            firebaseTracks.tracks!!.map {
                Track.create(
                        it.id!!,
                        it.name!!,
                        Optional.fromNullable(it.accent_color),
                        Optional.fromNullable(it.text_color),
                        Optional.fromNullable(it.icon_url)
                )
            }
        }

    private val combineIntoEvent: (FirestoreEvent, List<Speaker>, FirebaseFavorites, List<Place>, List<Track>, DateTimeZone) -> Event
        get() = { apiEvent, speakers, favorites, places, tracks, timeZone ->
            Event.create(
                    apiEvent.id,
                    checksum.getChecksumOf(apiEvent.id),
                    LocalDateTime(apiEvent.startTime),
                    LocalDateTime(apiEvent.endTime),
                    apiEvent.title,
                    placeById(places, apiEvent.place),
                    Optional.fromNullable<String>(apiEvent.experienceLevel).flatMap { ExperienceLevel.tryParsingFrom(it) },
                    speakersByIds(speakers, apiEvent.speakers.map { it.id }),
                    Event.Type.fromRawType(apiEvent.type),
                    favorites.hasFavorite(apiEvent.id),
                    Optional.fromNullable(apiEvent.description),
                    trackById(tracks, apiEvent.track),
                    timeZone
            )
        }

    private fun placeById(places: List<Place>, place: FirestorePlace?): Optional<Place> {
        return places.find { it.id == place?.id }.optional()
    }

    private fun trackById(tracks: List<Track>, track: FirestoreTrack?): Optional<Track> {
        return tracks.find { it.id == track?.id }.optional()
    }

    override fun events(userId: String): Observable<List<Event>> {
        val sessionsObservable = firestoreDbService.events()
        val speakersObservable = speakerRepository.speakers()
        val favoritesObservable = dbService.favorites(userId)
        val placesObservable = dbService.places().map(toPlaces())                 // TODO access them by ID directly?
        val tracksObservable = dbService.tracks().map(toTracks)                 // TODO extract repositories?
        val timeZoneObservable = dbService.venueInfo().map(toTimeZone())

        return Observable.combineLatest(
                sessionsObservable,
                speakersObservable,
                favoritesObservable,
                placesObservable,
                tracksObservable,
                timeZoneObservable,
                combineIntoEvents
        )
    }

    private val combineIntoEvents: Function6<List<FirestoreEvent>, List<Speaker>, FirebaseFavorites, List<Place>, List<Track>, DateTimeZone,
            List<Event>>
        get() = Function6 { firebaseEvents, speakers, favorites, places, tracks, timeZone ->
            firebaseEvents.map(combineEventWith(speakers, favorites, places, tracks, timeZone))
        }

    private fun combineEventWith(
            speakers: List<Speaker>,
            favorites: FirebaseFavorites,
            places: List<Place>,
            tracks: List<Track>,
            timeZone: DateTimeZone
    ): ((FirestoreEvent) -> Event) {
        return { apiEvent ->
            Event.create(
                    apiEvent.id,
                    checksum.getChecksumOf(apiEvent.id),
                    LocalDateTime(apiEvent.startTime),
                    LocalDateTime(apiEvent.endTime),
                    apiEvent.title,
                    placeById(places, apiEvent.place),
                    Optional.fromNullable<String>(apiEvent.experienceLevel).flatMap { ExperienceLevel.tryParsingFrom(it) },
                    speakersByIds(speakers, apiEvent.speakers.map { it.id }),
                    Event.Type.fromRawType(apiEvent.type),
                    favorites.hasFavorite(apiEvent.id),
                    Optional.fromNullable(apiEvent.description),
                    trackById(tracks, apiEvent.track),
                    timeZone
            )
        }
    }

    private fun speakersByIds(speakers: List<Speaker>, speakerIds: List<String>?): List<Speaker> {
        if (speakerIds == null || speakerIds.isEmpty()) {
            return emptyList()
        } else {
            return speakers.filter { speakerIds.contains(it.id) }
        }
    }

    override fun addFavorite(eventId: String, userId: String): Completable {
        return dbService.addFavorite(eventId, userId)
    }

    override fun removeFavorite(eventId: String, userId: String): Completable {
        return dbService.removeFavorite(eventId, userId)
    }
}
