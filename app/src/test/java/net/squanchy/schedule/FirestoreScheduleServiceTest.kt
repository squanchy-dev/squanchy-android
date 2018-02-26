package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.BehaviorSubject
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.schedule.domain.view.aTrack
import net.squanchy.schedule.domain.view.anEvent
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.aFirestoreEvent
import net.squanchy.service.firestore.aFirestoreSchedulePage
import net.squanchy.service.firestore.aFirestoreTrack
import net.squanchy.service.firestore.toTrack
import net.squanchy.service.repository.TrackFilter
import net.squanchy.support.lang.Checksum
import net.squanchy.support.lang.Optional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
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
    private lateinit var trackFilter: TrackFilter

    @Mock
    private lateinit var checksum: Checksum

    private lateinit var scheduleService: FirestoreScheduleService

    @Before
    fun init() {
        scheduleService = FirestoreScheduleService(authService, dbService, trackFilter, checksum)
        `when`(dbService.timezone()).thenReturn(Observable.just(DateTimeZone.UTC))
        `when`(checksum.getChecksumOf(anyString())).thenReturn(1234L)
    }

    @Test
    fun `verify unselected tracks are excluded from the schedule`() {
        val schedule = aFirestoreSchedulePage(
            events = listOf(
                aFirestoreEvent(track = aFirestoreTrack(id = "ABC")),
                aFirestoreEvent(track = aFirestoreTrack()),
                aFirestoreEvent(track = aFirestoreTrack()),
                aFirestoreEvent(track = aFirestoreTrack(id = "BDE")),
                aFirestoreEvent(track = aFirestoreTrack(id = "ABC"))
            )
        )

        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(schedule)))
        `when`(trackFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(setOf(aFirestoreTrack(id = "ABC").toTrack())))

        val result = scheduleService.schedule(false)
            .test()
            .values()
            .first()

        val tracksFromSchedule = result
            .pages
            .map { it.events }
            .flatten()
            .map { it.track }
            .filter { it.isPresent }
            .map { it.get() }

        assertTrue(tracksFromSchedule.all { it.id == "ABC" })
    }

    @Test
    fun `verify events with no track are not excluded from the schedule`() {
        val schedulePage = aFirestoreSchedulePage(
            events = listOf(
                aFirestoreEvent(track = aFirestoreTrack(id = "ABC")),
                aFirestoreEvent(track = null),
                aFirestoreEvent(track = null),
                aFirestoreEvent(track = aFirestoreTrack(id = "BDE")),
                aFirestoreEvent(track = aFirestoreTrack(id = "ABC"))
            )
        )
        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(schedulePage)))
        `when`(trackFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(setOf(aFirestoreTrack(id = "ABC").toTrack())))
        val subscription = TestObserver<Schedule>()

        scheduleService.schedule(onlyFavorites = false)
            .subscribe(subscription)

        subscription.assertValue(Schedule(
            listOf(SchedulePage("dayId", LocalDate.now(), listOf(
                anEvent(track = Optional.of(aTrack(id = "ABC"))),
                anEvent(track = Optional.absent()),
                anEvent(track = Optional.absent())
            ))), DateTimeZone.UTC)
        )
    }
}
