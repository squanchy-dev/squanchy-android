package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.schedule.domain.view.Track
import net.squanchy.schedule.filterschedule.TracksFilter
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.schedule.FirestoreEvent
import net.squanchy.service.firestore.model.schedule.FirestoreSchedulePage
import net.squanchy.service.firestore.toEvent
import net.squanchy.support.lang.Checksum
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate

interface ScheduleService {
    fun schedule(onlyFavorites: Boolean = false): Observable<Schedule>
    fun currentUserIsSignedIn(): Observable<Boolean>
}

class FirestoreScheduleService(
    private val authService: FirebaseAuthService,
    private val dbService: FirestoreDbService,
    private val tracksFilter: TracksFilter,
    private val checksum: Checksum
) : ScheduleService {

    override fun schedule(onlyFavorites: Boolean): Observable<Schedule> {
        val filteredDbSchedulePages = dbService.scheduleView()
            .filterByFavorites(onlyFavorites)
            .filterByTracks(tracksFilter.selectedTracks)

        val domainSchedulePages = Observable.combineLatest(
            filteredDbSchedulePages,
            dbService.timezone(),
            toSortedDomainSchedulePages()
        )

        return Observable.combineLatest(domainSchedulePages, dbService.timezone(), combineInAPair())
            .map { schedulePagesAndTimeZone ->
                val (schedulePages, timeZone) = schedulePagesAndTimeZone
                Pair(schedulePages, timeZone)
            }
            .map { pagesAndTimeZone -> Schedule(pagesAndTimeZone.first, pagesAndTimeZone.second) }
    }

    private fun Observable<List<FirestoreSchedulePage>>.filterByFavorites(onlyFavorites: Boolean) =
        when {
            onlyFavorites -> this.removeNonFavorites()
            else -> this
        }

    private fun Observable<List<FirestoreSchedulePage>>.removeNonFavorites(): Observable<List<FirestoreSchedulePage>> =
        map { pages: List<FirestoreSchedulePage> -> pages.filterPagesEvents { it.isFavorite() } }

    private fun FirestoreEvent.isFavorite() = true // TODO add actual favourites filtering

    private fun Observable<List<FirestoreSchedulePage>>.filterByTracks(selectedTracks: Observable<Set<Track>>) =
        Observable.combineLatest(
            this,
            selectedTracks,
            BiFunction { pages: List<FirestoreSchedulePage>, tracks: Set<Track> ->
                pages.filterPagesEvents { it.hasTrackOrNoTrack(tracks) }
            }
        )

    private fun List<FirestoreSchedulePage>.filterPagesEvents(predicate: (FirestoreEvent) -> Boolean) =
        map { it.filterPageEvents(predicate) }

    private fun FirestoreSchedulePage.filterPageEvents(predicate: (FirestoreEvent) -> Boolean): FirestoreSchedulePage {
        val filteredEvents = events.filter(predicate)
        return FirestoreSchedulePage().apply {
            events = filteredEvents
            day = this@filterPageEvents.day
        }
    }

    private fun FirestoreEvent.hasTrackOrNoTrack(allowedTracks: Set<Track>) =
        track?.let { allowedTracks.any { it.id == this.track?.id ?: it.id } } ?: true

    private fun toSortedDomainSchedulePages() =
        BiFunction<List<FirestoreSchedulePage>, DateTimeZone, List<SchedulePage>> { pages, timeZone ->
            pages.toSortedDomainSchedulePages(checksum, timeZone)
        }

    private fun List<FirestoreSchedulePage>.toSortedDomainSchedulePages(checksum: Checksum, timeZone: DateTimeZone) =
        map { page -> page.toSortedDomainSchedulePage(checksum, timeZone) }
            .sortedBy { page -> page.date }

    private fun FirestoreSchedulePage.toSortedDomainSchedulePage(checksum: Checksum, timeZone: DateTimeZone): SchedulePage =
        SchedulePage(day.id, LocalDate(day.date), events.map { it.toEvent(checksum, timeZone) }.sortedBy { it.startTime })

    private fun <A, B> combineInAPair(): BiFunction<A, B, Pair<A, B>> = BiFunction { a, b -> Pair(a, b) }

    override fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }
}
