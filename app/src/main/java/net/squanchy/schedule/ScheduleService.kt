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
            .map {
                it.map { schedulePage ->
                    SchedulePage(
                            schedulePage.day.id,
                            LocalDate(schedulePage.day.date),
                            schedulePage.events.map {
                                Event(
                                        it.id,
                                        checksum.getChecksumOf(it.id),
                                        LocalDateTime(it.startTime),
                                        LocalDateTime(it.endTime),
                                        it.title,
                                        it.place.optional().map { Place(it.id, it.name, it.floor.optional()) },
                                        it.track.optional().map {
                                            Track(
                                                    it.id,
                                                    it.name,
                                                    it.accent_color.optional(),
                                                    it.text_color.optional(),
                                                    it.icon_url.optional()
                                            )
                                        },
                                        it.speakers.map {
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
                                        },
                                        ExperienceLevel.tryParsingFrom(it.experienceLevel),
                                        schedulePage.day.id,
                                        Event.Type.fromRawType(it.type),
                                        false, // TODO
                                        it.description.optional(),
                                        DateTimeZone.UTC // TODO
                                )
                            }
                                .sortedBy { it.startTime }
                                .let {
                                    when {
                                        onlyFavorites -> it.filter { it.favorited }
                                        else -> it
                                    }
                                }
                    )
                }
            }
            .map { Schedule(it) }
    }

    override fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }

}
