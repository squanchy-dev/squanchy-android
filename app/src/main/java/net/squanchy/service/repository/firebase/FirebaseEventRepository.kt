package net.squanchy.service.repository.firebase

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.functions.Function3
import io.reactivex.functions.Function6
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firebase.FirebaseDbService
import net.squanchy.service.firebase.model.FirebaseEvent
import net.squanchy.service.firebase.model.FirebaseEvents
import net.squanchy.service.firebase.model.FirebaseFavorites
import net.squanchy.service.firebase.model.FirebasePlace
import net.squanchy.service.firebase.model.FirebasePlaces
import net.squanchy.service.firebase.model.FirebaseTrack
import net.squanchy.service.firebase.model.FirebaseTracks
import net.squanchy.service.firebase.model.FirebaseVenue
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.FirestorePlace
import net.squanchy.service.firestore.model.FirestoreSpeaker
import net.squanchy.service.firestore.model.FirestoreSubmission
import net.squanchy.service.firestore.model.FirestoreTrack
import net.squanchy.service.firestore.model.FirestoreUser
import net.squanchy.service.firestore.model.getOptionalWithId
import net.squanchy.service.firestore.model.getWithId
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.lang.Checksum
import net.squanchy.support.lang.Func1
import net.squanchy.support.lang.Lists
import net.squanchy.support.lang.Lists.*
import net.squanchy.support.lang.Optional
import net.squanchy.support.lang.asOptional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime

class FirebaseEventRepository(
        private val dbService: FirebaseDbService,
        private val firestoreDbService: FirestoreDbService,
        private val checksum: Checksum,
        private val speakerRepository: SpeakerRepository
) : EventRepository {

    override fun event(id: String, userId: String): Observable<Event> {
        val eventId = "0JHaLBEHTZCijrrvIIgf"
        return firestoreDbService.event(eventId)
            .flatMap {
                val places = it.place.getOptionalWithId(FirestorePlace::class.java)

                val submissionAndSpeakers: Observable<Pair<FirestoreSubmission, List<Pair<FirestoreSpeaker, FirestoreUser>>>> = it.submission.getWithId(FirestoreSubmission::class.java)
                    .flatMap { submission ->
                        Observable.zip(
                                submission.speakers.map {
                                    it.getWithId(FirestoreSpeaker::class.java).flatMap { speaker ->
                                        speaker.user_profile.getWithId(FirestoreUser::class.java).map { Pair(speaker, it) }
                                    }
                                },
                                {
                                    it.map {
                                        @Suppress("UNCHECKED_CAST")
                                        it as Pair<FirestoreSpeaker, FirestoreUser>
                                    }
                                }
                        ).map { Pair(submission, it) }
                    }
                val tracks = it.track.getOptionalWithId(FirestoreTrack::class.java)
                Observable.combineLatest(
                        places,
                        submissionAndSpeakers,
                        tracks,
                        Function3<Optional<FirestorePlace>, Pair<FirestoreSubmission, List<Pair<FirestoreSpeaker, FirestoreUser>>>,
                                Optional<FirestoreTrack>, Event>
                        { placeOptional, (submission, speakers), trackOptional ->
                            val place = placeOptional.map {
                                Place(
                                        it.id,
                                        it.name,
                                        it.floor.asOptional()
                                )
                            }
                            val track = trackOptional.map {
                                Track(
                                        it.id,
                                        it.name,
                                        it.accent_color.asOptional(),
                                        it.text_color.asOptional(),
                                        it.icon_url.asOptional()
                                )
                            }
                            Event(
                                    eventId,
                                    checksum.getChecksumOf(eventId),
                                    LocalDateTime(it.start_time),
                                    LocalDateTime(it.end_time),
                                    submission.title,
                                    place,
                                    track,
                                    speakers.map { (speaker, user) ->
                                        Speaker(
                                                checksum.getChecksumOf(speaker.id),
                                                speaker.id,
                                                user.full_name,
                                                speaker.bio,
                                                speaker.company_name.asOptional(),
                                                speaker.company_url.asOptional(),
                                                speaker.personal_url.asOptional(),
                                                user.profile_pic.asOptional(),
                                                speaker.twitter_handle.asOptional()
                                        )
                                    },
                                    Optional.absent(),
                                    it.day.id,
                                    Event.Type.fromRawType(it.type),
                                    false,
                                    submission.abstract.asOptional(),
                                    DateTimeZone.UTC


                            )
                        }
                )
            }

    }

    private fun toTimeZone(): Function<FirebaseVenue, DateTimeZone> {
        return Function { firebaseVenue -> DateTimeZone.forID(firebaseVenue.timezone) }
    }

    private fun toPlaces(): Function<FirebasePlaces, List<Place>> {
        return Function { firebasePlaces ->
            Lists.map<FirebasePlace, Place>(
                    firebasePlaces.places
            ) { firebasePlace ->
                Place.create(
                        firebasePlace.id!!,
                        firebasePlace.name!!,
                        Optional.fromNullable(firebasePlace.floor)
                )
            }
        }
    }

    private fun toTracks(): Function<FirebaseTracks, List<Track>> {
        return Function { firebaseTracks ->
            Lists.map<FirebaseTrack, Track>(
                    firebaseTracks.tracks
            ) { firebaseTrack ->
                Track.create(
                        firebaseTrack.id!!,
                        firebaseTrack.name!!,
                        Optional.fromNullable(firebaseTrack.accent_color),
                        Optional.fromNullable(firebaseTrack.text_color),
                        Optional.fromNullable(firebaseTrack.icon_url)
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
        return Lists.find(places) { (id) -> id == placeId }
    }

    private fun trackById(tracks: List<Track>, trackId: String?): Optional<Track> {
        return Lists.find(tracks) { (id) -> id == trackId }
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
                combineIntoEvents()
        )
    }

    private fun combineIntoEvents(): Function6<FirebaseEvents, List<Speaker>, FirebaseFavorites, List<Place>, List<Track>, DateTimeZone, List<Event>> {
        return Function6 { firebaseEvents, speakers, favorites, places, tracks, timeZone ->
            Lists.map(
                    firebaseEvents.events!!.values.toMutableList(),
                    combineEventWith(speakers, favorites, places, tracks, timeZone)
            )
        }
    }

    private fun combineEventWith(
            speakers: List<Speaker>,
            favorites: FirebaseFavorites,
            places: List<Place>,
            tracks: List<Track>,
            timeZone: DateTimeZone
    ): Func1<FirebaseEvent, Event> {
        return Func1 { apiEvent ->
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
        return if (speakerIds == null || speakerIds.isEmpty()) {
            emptyList()
        } else filter(speakers) { (_, id) -> speakerIds.contains(id) }

    }

    override fun addFavorite(eventId: String, userId: String): Completable {
        return dbService.addFavorite(eventId, userId)
    }

    override fun removeFavorite(eventId: String, userId: String): Completable {
        return dbService.removeFavorite(eventId, userId)
    }
}
