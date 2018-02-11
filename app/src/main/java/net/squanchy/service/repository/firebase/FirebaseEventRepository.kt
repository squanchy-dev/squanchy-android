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
import net.squanchy.service.firebase.model.FirebaseEvent
import net.squanchy.service.firebase.model.FirebaseEvents
import net.squanchy.service.firebase.model.FirebaseFavorites
import net.squanchy.service.firebase.model.FirebasePlaces
import net.squanchy.service.firebase.model.FirebaseTracks
import net.squanchy.service.firebase.model.FirebaseVenue
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
        private val checksum: Checksum,
        private val speakerRepository: SpeakerRepository
) : EventRepository {

    override fun event(eventId: String, userId: String): Observable<Event> {
        val eventObservable = dbService.event(eventId)
        val speakersObservable = speakerRepository.speakers()
        val favoritesObservable = dbService.favorites(userId)
        val placesObservable = dbService.places().map(toPlaces())                 // TODO access them by ID directly?
        val tracksObservable = dbService.tracks().map(toTracks())                 // TODO extract repositories?
        val timeZoneObservable = dbService.venueInfo().map(toTimeZone())

        return Observable.combineLatest(
                eventObservable,
                speakersObservable,
                favoritesObservable,
                placesObservable,
                tracksObservable,
                timeZoneObservable,
                combineIntoEvent()
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

    private fun toTracks(): Function<FirebaseTracks, List<Track>> {
        return Function { firebaseTracks ->
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
    }

    private fun combineIntoEvent(): Function6<FirebaseEvent, List<Speaker>, FirebaseFavorites, List<Place>, List<Track>, DateTimeZone, Event> {
        return Function6 { apiEvent, speakers, favorites, places, tracks, timeZone ->
            Event.create(
                    apiEvent.id!!,
                    checksum.getChecksumOf(apiEvent.id!!),
                    apiEvent.day_id!!,
                    LocalDateTime(apiEvent.start_time),
                    LocalDateTime(apiEvent.end_time),
                    apiEvent.name!!,
                    placeById(places, apiEvent.place_id),
                    Optional.fromNullable<String>(apiEvent.experience_level).flatMap { ExperienceLevel.tryParsingFrom(it) },
                    speakersByIds(speakers, apiEvent.speaker_ids),
                    Event.Type.fromRawType(apiEvent.type!!),
                    favorites.hasFavorite(apiEvent.id!!),
                    Optional.fromNullable(apiEvent.description),
                    trackById(tracks, apiEvent.track_id),
                    timeZone
            )
        }
    }

    private fun placeById(places: List<Place>, placeId: String?): Optional<Place> {
        return places.find { it.id == placeId }.optional()
    }

    private fun trackById(tracks: List<Track>, trackId: String?): Optional<Track> {
        return tracks.find { it.id == trackId }.optional()
    }

    override fun events(userId: String): Observable<List<Event>> {
        val sessionsObservable = dbService.events()
        val speakersObservable = speakerRepository.speakers()
        val favoritesObservable = dbService.favorites(userId)
        val placesObservable = dbService.places().map(toPlaces())                 // TODO access them by ID directly?
        val tracksObservable = dbService.tracks().map(toTracks())                 // TODO extract repositories?
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

    private val combineIntoEvents: Function6<FirebaseEvents, List<Speaker>, FirebaseFavorites, List<Place>, List<Track>, DateTimeZone, List<Event>>
        get() = Function6 { firebaseEvents, speakers, favorites, places, tracks, timeZone ->
            firebaseEvents.events!!.values.map(combineEventWith(speakers, favorites, places, tracks, timeZone))
        }

    private fun combineEventWith(
            speakers: List<Speaker>,
            favorites: FirebaseFavorites,
            places: List<Place>,
            tracks: List<Track>,
            timeZone: DateTimeZone
    ): ((FirebaseEvent) -> Event) {
        return { apiEvent ->
            Event.create(
                    apiEvent.id!!,
                    checksum.getChecksumOf(apiEvent.id!!),
                    apiEvent.day_id!!,
                    LocalDateTime(apiEvent.start_time),
                    LocalDateTime(apiEvent.end_time),
                    apiEvent.name!!,
                    placeById(places, apiEvent.place_id),
                    Optional.fromNullable<String>(apiEvent.experience_level).flatMap { ExperienceLevel.tryParsingFrom(it) },
                    speakersByIds(speakers, apiEvent.speaker_ids),
                    Event.Type.fromRawType(apiEvent.type!!),
                    favorites.hasFavorite(apiEvent.id!!),
                    Optional.fromNullable(apiEvent.description),
                    trackById(tracks, apiEvent.track_id),
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
