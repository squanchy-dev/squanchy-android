package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.functions.Function3
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.schedule.FirestoreSchedulePage
import net.squanchy.service.firestore.toEvent
import net.squanchy.service.repository.FilterScheduleRepository
import net.squanchy.support.lang.Checksum
import net.squanchy.support.lang.Optional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate

interface ScheduleService {
    fun schedule(onlyFavorites: Boolean): Observable<Schedule>
    fun currentUserIsSignedIn(): Observable<Boolean>
}

class FirestoreScheduleService(
    private val authService: FirebaseAuthService,
    private val dbService: FirestoreDbService,
    private val filterScheduleRepository: FilterScheduleRepository,
    private val checksum: Checksum
) : ScheduleService {

    override fun schedule(onlyFavorites: Boolean): Observable<Schedule> {
        return Observable.combineLatest(dbService.scheduleView(), dbService.timezone(), filterScheduleRepository.filters, combineInATriple())
            .map { scheduleViewTimeZoneAndFilters ->
                val (scheduleView, timeZone, filters) = scheduleViewTimeZoneAndFilters
                val schedulePages = createSchedulePages(scheduleView, timeZone, onlyFavorites, filters, checksum)
                Pair(schedulePages, timeZone)
            }
            .map { pagesAndTimeZone -> Schedule(pagesAndTimeZone.first, pagesAndTimeZone.second) }
    }

    private fun createSchedulePages(
        schedulePages: List<FirestoreSchedulePage>,
        timeZone: DateTimeZone,
        onlyFavorites: Boolean,
        filters: Set<Track>,
        checksum: Checksum
    ) = schedulePages.map { schedulePage ->
        SchedulePage(
            schedulePage.day.id,
            LocalDate(schedulePage.day.date),
            // TODO we don't have favourites yet (they're all "false")
            schedulePage.events.map { it.toEvent(checksum, timeZone) }
                .sortedBy { it.startTime }
                .filterOnlyFavorites(onlyFavorites)
                .filterByTracks(filters)
        )
    }

    private fun <T, U, V> combineInATriple(): Function3<List<T>, U, Set<V>, Triple<List<T>, U, Set<V>>> = Function3(::Triple)

    private fun List<Event>.filterOnlyFavorites(onlyFavorites: Boolean) = when {
        onlyFavorites -> filter { it.favorited }
        else -> this
    }

    private fun List<Event>.filterByTracks(tracks: Set<Track>) = when {
        tracks.isEmpty() -> this
        else -> filter { shouldIncludeEvent(it, tracks) }
    }

    private fun shouldIncludeEvent(event: Event, tracks: Set<Track>): Boolean = when (event.track) {
        Optional.absent<Track>() -> true
        else -> tracks.contains(event.track.get())
    }

    override fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }
}
