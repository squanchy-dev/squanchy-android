package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.schedule.FirestoreEvent
import net.squanchy.service.firestore.model.schedule.FirestoreSchedulePage
import net.squanchy.service.firestore.toEvent
import net.squanchy.service.repository.TrackFilter
import net.squanchy.support.lang.Checksum
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate

interface ScheduleService {
    fun schedule(onlyFavorites: Boolean): Observable<Schedule>
    fun currentUserIsSignedIn(): Observable<Boolean>
}

class FirestoreScheduleService(
    private val authService: FirebaseAuthService,
    private val dbService: FirestoreDbService,
    private val trackFilter: TrackFilter,
    private val checksum: Checksum
) : ScheduleService {

    override fun schedule(onlyFavorites: Boolean): Observable<Schedule> {
        val filteredDbSchedulePages = dbService.scheduleView()
            .filterByFavorites(onlyFavorites)
            .filterByTracks(trackFilter.selectedTracks)

        val domainSchedulePages = Observable.combineLatest(
            filteredDbSchedulePages,
            dbService.timezone(),
            toDomainSchedulePages()
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
                when {
                    tracks.isEmpty() -> emptyList()
                    else -> pages.filterPagesEvents { event -> tracks.any { it.id == event.track?.id } }
                }
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

    private fun toDomainSchedulePages() =
        BiFunction<List<FirestoreSchedulePage>, DateTimeZone, List<SchedulePage>> { pages, timeZone ->
            pages.toDomainSchedulePages(checksum, timeZone)
        }

    private fun List<FirestoreSchedulePage>.toDomainSchedulePages(checksum: Checksum, timeZone: DateTimeZone) =
        map { it.toDomainSchedulePage(checksum, timeZone) }

    private fun FirestoreSchedulePage.toDomainSchedulePage(checksum: Checksum, timeZone: DateTimeZone): SchedulePage =
        SchedulePage(day.id, LocalDate(day.date), events.map { it.toEvent(checksum, timeZone) })

    private fun <A, B> combineInAPair(): BiFunction<A, B, Pair<A, B>> = BiFunction { a, b -> Pair(a, b) }

    override fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }
}
