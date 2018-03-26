package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.schedule.domain.view.Track
import net.squanchy.schedule.tracksfilter.TracksFilter
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.firebase.model.schedule.FirestoreEvent
import net.squanchy.service.firebase.model.schedule.FirestoreFavorite
import net.squanchy.service.firebase.model.schedule.FirestoreSchedulePage
import net.squanchy.service.firebase.toEvent
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

    override fun schedule(onlyFavorites: Boolean): Observable<Schedule> = schedule(onlyFavorites, Schedulers.io())

    fun schedule(onlyFavorites: Boolean, observeScheduler: Scheduler): Observable<Schedule> {
        val filteredDbSchedulePages = dbService.scheduleView()
            .observeOn(observeScheduler)
            .filterByFavorites(onlyFavorites)
            .filterByTracks(tracksFilter.selectedTracks)

        val domainSchedulePages = Observable.combineLatest(
            filteredDbSchedulePages,
            dbService.timezone(),
            toSortedDomainSchedulePages()
        )

        return Observable.combineLatest(domainSchedulePages, dbService.timezone(), BiFunction(::Schedule))
    }

    private fun Observable<List<FirestoreSchedulePage>>.filterByFavorites(onlyFavorites: Boolean) =
        when {
            onlyFavorites -> this.removeNonFavorites()
            else -> this
        }

    private fun Observable<List<FirestoreSchedulePage>>.removeNonFavorites(): Observable<List<FirestoreSchedulePage>> {
        return Observable.combineLatest(
            this,
            authService.ifUserSignedInThenObservableFrom(dbService::favorites),
            BiFunction { schedule, favorites ->
                schedule.filterPagesEvents { favorites.includes(it.id) }
            })
    }

    private fun List<FirestoreFavorite>.includes(eventId: String) =
        mapNotNull { it.id }.contains(eventId)

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
        track?.let { eventTrack -> allowedTracks.any { it.id == eventTrack.id } } ?: true

    private fun toSortedDomainSchedulePages() =
        BiFunction<List<FirestoreSchedulePage>, DateTimeZone, List<SchedulePage>> { pages, timeZone ->
            pages.toSortedDomainSchedulePages(checksum, timeZone)
        }

    private fun List<FirestoreSchedulePage>.toSortedDomainSchedulePages(checksum: Checksum, timeZone: DateTimeZone) =
        map { page -> page.toSortedDomainSchedulePage(checksum, timeZone) }
            .sortedBy(SchedulePage::date)

    private fun FirestoreSchedulePage.toSortedDomainSchedulePage(checksum: Checksum, timeZone: DateTimeZone): SchedulePage =
        SchedulePage(day.id, LocalDate(day.date), events.map { it.toEvent(checksum, timeZone) }
            .sortedBy(Event::startTime))

    override fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }
}
