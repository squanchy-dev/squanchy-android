package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
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
import net.squanchy.service.firestore.toEvent
import net.squanchy.service.firestore.toPlace
import net.squanchy.service.firestore.toSpeaker
import net.squanchy.service.firestore.toTrack
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
        return Observable.combineLatest(dbService.scheduleView(), dbService.timezone(), combineInAPair())
            .map { pagesAndTimeZone ->

                val (schedulePages, timeZone) = pagesAndTimeZone
                schedulePages.map { schedulePage ->
                    SchedulePage(
                        schedulePage.day.id,
                        LocalDate(schedulePage.day.date),
                        schedulePage.events.map { it.toEvent(checksum, timeZone) }
                            .sortedBy { it.startTime }
                            .filterOnlyFavorites(onlyFavorites)
                    )
                }
            }
            .map(::Schedule)
    }

    private fun <T, U> combineInAPair(): BiFunction<List<T>, U, Pair<List<T>, U>> = BiFunction(::Pair)

    private fun List<Event>.filterOnlyFavorites(onlyFavorites: Boolean) = when {
        onlyFavorites -> filter { it.favorited }
        else -> this
    }

    override fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }
}
