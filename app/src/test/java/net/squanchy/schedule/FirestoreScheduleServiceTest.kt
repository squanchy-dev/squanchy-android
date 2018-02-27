package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.BehaviorSubject
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.schedule.domain.view.Track
import net.squanchy.schedule.domain.view.aTrack
import net.squanchy.schedule.domain.view.anEvent
import net.squanchy.schedule.filterschedule.TracksFilter
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.aFirestoreEvent
import net.squanchy.service.firestore.aFirestoreSchedulePage
import net.squanchy.service.firestore.aFirestoreSpeaker
import net.squanchy.service.firestore.aFirestoreTrack
import net.squanchy.support.lang.Checksum
import net.squanchy.support.lang.Optional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class FirestoreScheduleServiceTest {

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
        `when`(dbService.timezone()).thenReturn(Observable.just(A_TIMEZONE))
        `when`(checksum.getChecksumOf(aFirestoreSpeaker().id)).thenReturn(5466L)
        `when`(checksum.getChecksumOf(aFirestoreEvent().id)).thenReturn(1234L)
    }

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
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(Schedule(
            listOf(SchedulePage("dayId", LocalDate(schedulePage.day.date), listOf(
                anEvent(track = Optional.absent()),
                anEvent(track = Optional.absent())
            ))), A_TIMEZONE)
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
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(Schedule(
            listOf(SchedulePage("dayId", LocalDate(schedulePage.day.date), listOf(
                anEvent(track = Optional.absent()),
                anEvent(track = Optional.absent())
            ))), A_TIMEZONE)
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

        subscription.assertValue(Schedule(
            listOf(SchedulePage("dayId", LocalDate(schedulePage.day.date), emptyList())), A_TIMEZONE)
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
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(Schedule(
            listOf(SchedulePage("dayId", LocalDate(schedulePage.day.date), listOf(
                anEvent(track = Optional.of(aTrack(id = "A"))),
                anEvent(track = Optional.of(aTrack(id = "C")))
            ))), A_TIMEZONE)
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
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(Schedule(
            listOf(SchedulePage("dayId", LocalDate(schedulePage.day.date), listOf(
                anEvent(track = Optional.absent())
            ))), A_TIMEZONE)
        )
    }

    companion object {
        private val A_TIMEZONE = DateTimeZone.forID("Europe/Rome")
    }
}
