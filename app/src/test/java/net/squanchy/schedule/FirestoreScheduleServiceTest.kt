package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.BehaviorSubject
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.Track
import net.squanchy.schedule.domain.view.aSchedule
import net.squanchy.schedule.domain.view.aSchedulePage
import net.squanchy.schedule.domain.view.aTrack
import net.squanchy.schedule.domain.view.anEvent
import net.squanchy.schedule.tracksfilter.TracksFilter
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.aFirestoreDay
import net.squanchy.service.firestore.aFirestoreEvent
import net.squanchy.service.firestore.aFirestoreSchedulePage
import net.squanchy.service.firestore.aFirestoreSpeaker
import net.squanchy.service.firestore.aFirestoreTrack
import net.squanchy.support.lang.Checksum
import net.squanchy.support.lang.Optional
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class FirestoreScheduleServiceTest {

    companion object {
        private val CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"))

        private val A_START_TIME = Date(123456)
        private val AN_END_TIME = Date(123999)
        private val A_LATER_START_TIME = Date(124000)
        private val A_LATER_END_TIME = Date(124999)
    }

    @Rule
    @JvmField
    var rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var authService: FirebaseAuthService

    @Mock
    private lateinit var dbService: FirestoreDbService

    @Mock
    private lateinit var tracksFilter: TracksFilter

    @Mock
    private lateinit var checksum: Checksum

    private lateinit var scheduleService: FirestoreScheduleService

    @Before
    fun init() {
        scheduleService = FirestoreScheduleService(authService, dbService, tracksFilter, checksum)
        `when`(dbService.timezone()).thenReturn(Observable.just(aSchedule().timeZone))
        `when`(checksum.getChecksumOf(aFirestoreSpeaker().id)).thenReturn(5466)
    }

    @Test
    fun `should sort schedule pages by date`() {
        val firstDaySchedulePage = aFirestoreSchedulePage(day = aFirestoreDay(id = "1", date = A_START_TIME.dateOnly()))
        val secondDaySchedulePage = aFirestoreSchedulePage(day = aFirestoreDay(id = "2", date = A_START_TIME.plusOneDay().dateOnly()))
        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(firstDaySchedulePage, secondDaySchedulePage)))
        val allowedTracks = setOf(aTrack())
        `when`(tracksFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(allowedTracks))
        `when`(checksum.getChecksumOf(anEvent().id)).thenReturn(1234)
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(
            aSchedule(listOf(
                aSchedulePage(dayId = "1", date = LocalDate(A_START_TIME.dateOnly())),
                aSchedulePage(dayId = "2", date = LocalDate(A_START_TIME.plusOneDay().dateOnly()))
            ))
        )
    }

    @Test
    fun `should sort events in each schedule page by start time`() {
        val firstDaySchedulePage = aFirestoreSchedulePage(
            day = aFirestoreDay(id = "1", date = A_START_TIME.dateOnly()),
            events = listOf(
                aFirestoreEvent(id = "1", startTime = A_START_TIME, endTime = AN_END_TIME),
                aFirestoreEvent(id = "2", startTime = A_LATER_START_TIME, endTime = A_LATER_END_TIME),
                aFirestoreEvent(id = "3", startTime = A_START_TIME, endTime = AN_END_TIME)
            )
        )
        val secondDaySchedulePage = aFirestoreSchedulePage(
            day = aFirestoreDay(id = "2", date = A_START_TIME.plusOneDay().dateOnly()),
            events = listOf(
                aFirestoreEvent(id = "4", startTime = A_START_TIME.plusOneDay(), endTime = AN_END_TIME.plusOneDay()),
                aFirestoreEvent(id = "6", startTime = A_LATER_START_TIME.plusOneDay(), endTime = A_LATER_END_TIME.plusOneDay()),
                aFirestoreEvent(id = "5", startTime = A_START_TIME.plusOneDay(), endTime = AN_END_TIME.plusOneDay())
            )
        )
        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(firstDaySchedulePage, secondDaySchedulePage)))
        val allowedTracks = setOf(aTrack(id = "a track id"))
        `when`(tracksFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(allowedTracks))
        `when`(checksum.getChecksumOf("1")).thenReturn(1)
        `when`(checksum.getChecksumOf("2")).thenReturn(2)
        `when`(checksum.getChecksumOf("3")).thenReturn(3)
        `when`(checksum.getChecksumOf("4")).thenReturn(4)
        `when`(checksum.getChecksumOf("5")).thenReturn(5)
        `when`(checksum.getChecksumOf("6")).thenReturn(6)
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(aSchedule(listOf(
            aSchedulePage(
                dayId = "1",
                date = LocalDate(A_START_TIME.dateOnly()),
                events = listOf(
                    anEvent(id = "1", numericId = 1, startTime = A_START_TIME.toLocalDateTime(), endTime = AN_END_TIME.toLocalDateTime()),
                    anEvent(id = "3", numericId = 3, startTime = A_START_TIME.toLocalDateTime(), endTime = AN_END_TIME.toLocalDateTime()),
                    anEvent(
                        id = "2",
                        numericId = 2,
                        startTime = A_LATER_START_TIME.toLocalDateTime(),
                        endTime = A_LATER_END_TIME.toLocalDateTime()
                    )
                )),
            aSchedulePage(
                dayId = "2",
                date = LocalDate(A_START_TIME.plusOneDay().dateOnly()),
                events = listOf(
                    anEvent(
                        id = "4",
                        numericId = 4,
                        startTime = A_START_TIME.plusOneDay().toLocalDateTime(),
                        endTime = AN_END_TIME.plusOneDay().toLocalDateTime()
                    ),
                    anEvent(
                        id = "5",
                        numericId = 5,
                        startTime = A_START_TIME.plusOneDay().toLocalDateTime(),
                        endTime = AN_END_TIME.plusOneDay().toLocalDateTime()
                    ),
                    anEvent(
                        id = "6",
                        numericId = 6,
                        startTime = A_LATER_START_TIME.plusOneDay().toLocalDateTime(),
                        endTime = A_LATER_END_TIME.plusOneDay().toLocalDateTime()
                    )
                ))
        )))
    }

    @Test
    fun `should not exclude any events from the schedule when not filtering only by favorites`() {
        val schedulePage = aFirestoreSchedulePage(
            events = listOf(
                aFirestoreEvent(id = "1", track = null),
                aFirestoreEvent(id = "2", track = null)
            )
        )
        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(schedulePage)))
        val allowedTracks = setOf(aTrack(id = "a track id"))
        `when`(tracksFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(allowedTracks))
        `when`(checksum.getChecksumOf("1")).thenReturn(1)
        `when`(checksum.getChecksumOf("2")).thenReturn(2)
        // TODO mark one of the two firestore events above as favorite
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(
            aSchedule(listOf(
                aSchedulePage(events = listOf(
                    anEvent(id = "1", numericId = 1, track = Optional.absent()),
                    anEvent(id = "2", numericId = 2, track = Optional.absent())
                ))
            ))
        )
    }

    // TODO test events are filtered out when onyFavorites and they're not favorited

    @Test
    fun `should not exclude events with no track from the schedule when filtering has at least a track`() {
        val schedulePage = aFirestoreSchedulePage(
            events = listOf(
                aFirestoreEvent(track = null),
                aFirestoreEvent(track = null)
            )
        )
        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(schedulePage)))
        val allowedTracks = setOf(aTrack(id = "A"), aTrack("C"))
        `when`(tracksFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(allowedTracks))
        `when`(checksum.getChecksumOf(aFirestoreEvent().id)).thenReturn(1)
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(
            aSchedule(listOf(
                aSchedulePage(events = listOf(
                    anEvent(track = Optional.absent(), numericId = 1),
                    anEvent(track = Optional.absent(), numericId = 1)
                ))
            ))
        )
    }

    @Test
    fun `should not exclude events with no track from the schedule when filtering has no tracks`() {
        val schedulePage = aFirestoreSchedulePage(
            events = listOf(
                aFirestoreEvent(track = null),
                aFirestoreEvent(track = null)
            )
        )
        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(schedulePage)))
        val allowedTracks = emptySet<Track>()
        `when`(tracksFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(allowedTracks))
        `when`(checksum.getChecksumOf(aFirestoreEvent().id)).thenReturn(1)
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(
            aSchedule(listOf(
                aSchedulePage(events = listOf(
                    anEvent(track = Optional.absent(), numericId = 1),
                    anEvent(track = Optional.absent(), numericId = 1)
                ))
            ))
        )
    }

    @Test
    fun `should exclude events with wrong track from the schedule when filtering has allowed tracks`() {
        val schedulePage = aFirestoreSchedulePage(
            events = listOf(
                aFirestoreEvent(track = aFirestoreTrack(id = "B"))
            )
        )
        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(schedulePage)))
        val allowedTracks = setOf(aTrack(id = "A"), aTrack("C"))
        `when`(tracksFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(allowedTracks))
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(
            aSchedule(listOf(
                aSchedulePage(events = emptyList())
            ))
        )
    }

    @Test
    fun `should include events with right track from the schedule when filtering has allowed tracks`() {
        val schedulePage = aFirestoreSchedulePage(
            events = listOf(
                aFirestoreEvent(track = aFirestoreTrack(id = "A")),
                aFirestoreEvent(track = aFirestoreTrack(id = "C"))
            )
        )
        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(schedulePage)))
        val allowedTracks = setOf(aTrack(id = "A"), aTrack("C"))
        `when`(tracksFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(allowedTracks))
        `when`(checksum.getChecksumOf(aFirestoreEvent().id)).thenReturn(1)
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(
            aSchedule(listOf(
                aSchedulePage(events = listOf(
                    anEvent(track = Optional.of(aTrack(id = "A")), numericId = 1),
                    anEvent(track = Optional.of(aTrack(id = "C")), numericId = 1)
                ))
            ))
        )
    }

    @Test
    fun `should exclude events with a track from the schedule when filtering has no allowed tracks`() {
        val schedulePage = aFirestoreSchedulePage(
            events = listOf(
                aFirestoreEvent(track = aFirestoreTrack(id = "A")),
                aFirestoreEvent(track = null),
                aFirestoreEvent(track = aFirestoreTrack(id = "C"))
            )
        )
        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(schedulePage)))
        val allowedTracks = emptySet<Track>()
        `when`(tracksFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(allowedTracks))
        `when`(checksum.getChecksumOf(aFirestoreEvent().id)).thenReturn(1)
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(
            aSchedule(listOf(
                aSchedulePage(events = listOf(
                    anEvent(track = Optional.absent(), numericId = 1)
                ))
            ))
        )
    }

    private fun Date.plusOneDay(): Date = CALENDAR.apply {
        clear()
        time = this@plusOneDay
        add(Calendar.DAY_OF_YEAR, 1)
    }.time

    private fun Date.dateOnly(): Date = CALENDAR.apply {
        clear()
        time = this@dateOnly
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    private fun Date.toLocalDateTime() = LocalDateTime(this)
}
