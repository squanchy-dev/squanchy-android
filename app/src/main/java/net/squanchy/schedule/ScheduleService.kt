package net.squanchy.schedule

import io.reactivex.Observable
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.schedule.FirestoreEvent
import net.squanchy.service.firestore.model.schedule.FirestorePlace
import net.squanchy.service.firestore.model.schedule.FirestoreSpeaker
import net.squanchy.service.firestore.model.schedule.FirestoreTrack
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.lang.Checksum
import net.squanchy.support.lang.optional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

interface ScheduleService {
    fun schedule(onlyFavorites: Boolean): Observable<Schedule>
    fun currentUserIsSignedIn(): Observable<Boolean>
}

class FirestoreScheduleService(
        private val authService: FirebaseAuthService,
        private val dbService: FirestoreDbService,
        private val checksum: Checksum
) : ScheduleService {

    override fun schedule(onlyFavorites: Boolean): Observable<Schedule> {
        return dbService.scheduleView()
            .map { schedulePages ->
                schedulePages.map { schedulePage ->
                    SchedulePage(
                            schedulePage.day.id,
                            LocalDate(schedulePage.day.date),
                            schedulePage.events.map { it.toEvent(schedulePage.day.id) }
                                .sortedBy { it.startTime }
                                .filterOnlyFavorites(onlyFavorites)
                    )
                }
            }
            .map { Schedule(it) }
    }

    private fun FirestoreEvent.toEvent(dayId: String) = Event(
            id,
            checksum.getChecksumOf(id),
            LocalDateTime(startTime),
            LocalDateTime(endTime),
            title,
            place.toPlace(),
            track.toTrack(),
            speakers.toSpeakersList(checksum),
            ExperienceLevel.tryParsingFrom(experienceLevel),
            dayId,
            Event.Type.fromRawType(type),
            false, // TODO
            description.optional(),
            DateTimeZone.UTC // TODO
    )

    private fun FirestoreTrack?.toTrack() = optional().map {
        Track(
                it.id,
                it.name,
                it.accent_color.optional(),
                it.text_color.optional(),
                it.icon_url.optional()
        )
    }

    private fun FirestorePlace?.toPlace() = optional().map {
        Place(it.id, it.name, it.floor.optional())
    }

    private fun List<FirestoreSpeaker>.toSpeakersList(checksum: Checksum) = map {
        Speaker(
                checksum.getChecksumOf(it.id),
                it.id,
                it.name,
                it.bio,
                it.companyName.optional(),
                it.companyUrl.optional(),
                it.personalUrl.optional(),
                it.photoUrl.optional(),
                it.twitterUsername.optional()
        )
    }

    private fun List<Event>.filterOnlyFavorites(onlyFavorites: Boolean) = when {
        onlyFavorites -> filter { it.favorited }
        else -> this
    }

    override fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }
}
