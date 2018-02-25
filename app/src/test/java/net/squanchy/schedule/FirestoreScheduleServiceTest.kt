package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.aFirestoreEvent
import net.squanchy.service.firestore.aFirestoreSchedulePage
import net.squanchy.service.firestore.aFirestoreTrack
import net.squanchy.service.firestore.toTrack
import net.squanchy.service.repository.FilterScheduleRepository
import net.squanchy.support.lang.Checksum
import org.joda.time.DateTimeZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class FirestoreScheduleServiceTest {

    @Rule
    @JvmField
    var rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var authService: FirebaseAuthService

    @Mock
    lateinit var dbService: FirestoreDbService

    @Mock
    lateinit var filterScheduleRepository: FilterScheduleRepository

    @Mock
    lateinit var checksum: Checksum

    lateinit var scheduleService: FirestoreScheduleService

    @Before
    fun init() {
        scheduleService = FirestoreScheduleService(authService, dbService, filterScheduleRepository, checksum)
        `when`(dbService.timezone()).thenReturn(Observable.just(DateTimeZone.UTC))
        `when`(checksum.getChecksumOf(anyString())).thenReturn(1234L)
    }

    @Test
    fun `verify unique tracks are extracted from the schedule and sent to the repository`() {
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
        `when`(filterScheduleRepository.filters).thenReturn(BehaviorSubject.createDefault(emptySet()))

        scheduleService.schedule(false)
            .test()

        verify(filterScheduleRepository).allTracks =
            listOf(aFirestoreTrack(id = "ABC"), aFirestoreTrack(), aFirestoreTrack(id = "BDE"))
                .map { it.toTrack() }
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
        `when`(filterScheduleRepository.filters).thenReturn(BehaviorSubject.createDefault(setOf(aFirestoreTrack(id = "ABC").toTrack())))

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
        val schedule = aFirestoreSchedulePage(
            events = listOf(
                aFirestoreEvent(track = aFirestoreTrack(id = "ABC")),
                aFirestoreEvent(track = null),
                aFirestoreEvent(track = null),
                aFirestoreEvent(track = aFirestoreTrack(id = "BDE")),
                aFirestoreEvent(track = aFirestoreTrack(id = "ABC"))
            )
        )

        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(schedule)))
        `when`(filterScheduleRepository.filters).thenReturn(BehaviorSubject.createDefault(setOf(aFirestoreTrack(id = "ABC").toTrack())))

        val result = scheduleService.schedule(false)
            .test()
            .values()
            .first()

        val tracksFromSchedule = result
            .pages
            .map { it.events }
            .flatten()
            .map { it.track }
            .filter { !it.isPresent }

        assertEquals(tracksFromSchedule.size, 2)
    }
}