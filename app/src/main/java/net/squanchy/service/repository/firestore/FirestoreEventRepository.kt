package net.squanchy.service.repository.firestore

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function3
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.FirestorePlace
import net.squanchy.service.firestore.model.FirestoreSpeaker
import net.squanchy.service.firestore.model.FirestoreSubmission
import net.squanchy.service.firestore.model.FirestoreTrack
import net.squanchy.service.firestore.model.FirestoreUser
import net.squanchy.service.firestore.model.getOptionalWithId
import net.squanchy.service.firestore.model.getWithId
import net.squanchy.service.repository.EventRepository
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.lang.Checksum
import net.squanchy.support.lang.Optional
import net.squanchy.support.lang.asOptional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime

class FirestoreEventRepository(
        private val firestoreDbService: FirestoreDbService,
        private val checksum: Checksum
) : EventRepository {
    override fun event(eventId: String, userId: String): Observable<Event> {
        return firestoreDbService.event(eventId)
            .flatMap {
                val places = it.place.getOptionalWithId(FirestorePlace::class.java)

                val submissionAndSpeakers = it.submission.getWithId(FirestoreSubmission::class.java)
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
                        Function3<Optional<FirestorePlace>, Pair<FirestoreSubmission, List<Pair<FirestoreSpeaker, FirestoreUser>>>, Optional<FirestoreTrack>,
                                Event>
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

    override fun events(userId: String): Observable<List<Event>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addFavorite(eventId: String, userId: String): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeFavorite(eventId: String, userId: String): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
