package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.schedule.domain.view.Track
import net.squanchy.schedule.tracksfilter.TracksFilter
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.firebase.model.schedule.FirestoreEvent
import net.squanchy.service.firebase.model.schedule.FirestoreFavorite
import net.squanchy.service.firebase.model.schedule.FirestoreSchedulePage
import net.squanchy.service.firebase.toEvent
import net.squanchy.service.repository.AuthService
import net.squanchy.support.checksum.Checksum
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate

interface ScheduleService {

    fun schedule(): Observable<Schedule>
}

class FirestoreScheduleService(
    private val authService: AuthService,
    private val dbService: FirestoreDbService,
    private val tracksFilter: TracksFilter,
    private val checksum: Checksum
) : ScheduleService {

    override fun schedule(): Observable<Schedule> {
        val filteredDbSchedulePages = dbService.scheduleView()
            .filterByTracks(tracksFilter.selectedTracks)

        val domainSchedulePages = Observable.combineLatest(
            filteredDbSchedulePages,
            dbService.timezone(),
            authService.ifUserSignedInThenObservableFrom(dbService::favorites),
            toSortedDomainSchedulePages()
        )

        return Observable.combineLatest(domainSchedulePages, dbService.timezone(), BiFunction(::Schedule))
    }

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
        Function3<List<FirestoreSchedulePage>, DateTimeZone, List<FirestoreFavorite>, List<SchedulePage>> { pages, timeZone, favorites ->
            pages.toSortedDomainSchedulePages(checksum, timeZone, favorites)
        }

    private fun List<FirestoreSchedulePage>.toSortedDomainSchedulePages(
        checksum: Checksum,
        timeZone: DateTimeZone,
        favorites: List<FirestoreFavorite>
    ) =
        map { page -> page.toSortedDomainSchedulePage(checksum, timeZone, favorites) }
            .sortedBy(SchedulePage::date)

    private fun FirestoreSchedulePage.toSortedDomainSchedulePage(
        checksum: Checksum,
        timeZone: DateTimeZone,
        favorites: List<FirestoreFavorite>
    ): SchedulePage = SchedulePage(
        day.id,
        LocalDate(day.date, timeZone),
        events.map { it.toEvent(checksum, timeZone, favorites) }
            .sortedByStartTimeAndRoom()
    )

    private fun FirestoreEvent.toEvent(checksum: Checksum, timeZone: DateTimeZone, favorites: List<FirestoreFavorite>) =
        this.toEvent(checksum, timeZone, favorites.any { favorite -> favorite.id == this.id })

    private fun List<Event>.sortedByStartTimeAndRoom() =
        sortedWith(compareBy(
            Event::startTime,
            { it.place.orNull()?.position ?: -1 }
        ))
}
