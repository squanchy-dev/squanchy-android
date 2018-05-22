package net.squanchy.schedule

import arrow.core.Option
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.squanchy.A_DATE
import net.squanchy.A_TIMEZONE
import net.squanchy.FakeAuthService
import net.squanchy.schedule.domain.view.Track
import net.squanchy.schedule.domain.view.aSchedule
import net.squanchy.schedule.domain.view.aSchedulePage
import net.squanchy.schedule.domain.view.aTrack
import net.squanchy.schedule.domain.view.anEvent
import net.squanchy.schedule.tracksfilter.TracksFilter
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.firebase.aFirestoreDay
import net.squanchy.service.firebase.aFirestoreEvent
import net.squanchy.service.firebase.aFirestoreFavorite
import net.squanchy.service.firebase.aFirestoreSchedulePage
import net.squanchy.service.firebase.aFirestoreSpeaker
import net.squanchy.service.firebase.aFirestoreTrack
import net.squanchy.support.checksum.Checksum
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class FirestoreScheduleServiceTest {

    companion object {
        private val UID = "uid"
        private val A_START_TIME = A_DATE
        private val AN_END_TIME = A_START_TIME.plusMinutes(1)
        private val A_LATER_START_TIME = A_START_TIME.plusMinutes(10)
        private val A_LATER_END_TIME = A_LATER_START_TIME.plusMinutes(1)
    }

    @Rule
    @JvmField
    var rule: MockitoRule = MockitoJUnit.rule()

    private var authService = FakeAuthService(UID)

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
        `when`(checksum.getChecksumOf("speaker_${aFirestoreSpeaker().id}")).thenReturn(5466)
        `when`(dbService.favorites(UID)).thenReturn(Observable.just(emptyList()))
    }

    @Test
    fun `should sort schedule pages by date`() {
        val firstDaySchedulePage = aFirestoreSchedulePage(day = aFirestoreDay(id = "1", date = A_START_TIME.toDate()))
        val secondDaySchedulePage = aFirestoreSchedulePage(day = aFirestoreDay(id = "2", date = A_START_TIME.plusDays(1).toDate()))
        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(firstDaySchedulePage, secondDaySchedulePage)))
        val allowedTracks = setOf(aTrack())
        `when`(tracksFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(allowedTracks))
        `when`(checksum.getChecksumOf("event_${aFirestoreEvent().id}")).thenReturn(1234)

        scheduleService.schedule()
            .test()
            .assertValue(
                aSchedule(
                    pages = listOf(
                        aSchedulePage(dayId = "1", date = A_START_TIME.toDateTime(A_TIMEZONE).toLocalDate()),
                        aSchedulePage(dayId = "2", date = A_START_TIME.plusDays(1).toDateTime(A_TIMEZONE).toLocalDate())
                    )
                )
            )
    }

    @Test
    fun `should sort events in each schedule page by start time`() {
        val firstDaySchedulePage = aFirestoreSchedulePage(
            day = aFirestoreDay(id = "1", date = A_START_TIME.toDate()),
            events = listOf(
                aFirestoreEvent(id = "1", startTime = A_START_TIME.toDate(), endTime = AN_END_TIME.toDate()),
                aFirestoreEvent(id = "2", startTime = A_LATER_START_TIME.toDate(), endTime = A_LATER_END_TIME.toDate()),
                aFirestoreEvent(id = "3", startTime = A_START_TIME.toDate(), endTime = AN_END_TIME.toDate())
            )
        )
        val secondDaySchedulePage = aFirestoreSchedulePage(
            day = aFirestoreDay(id = "2", date = A_START_TIME.plusDays(1).toDate()),
            events = listOf(
                aFirestoreEvent(
                    id = "4",
                    startTime = A_START_TIME.plusDays(1).toDate(),
                    endTime = AN_END_TIME.plusDays(1).toDate()
                ),
                aFirestoreEvent(
                    id = "6",
                    startTime = A_LATER_START_TIME.plusDays(1).toDate(),
                    endTime = A_LATER_END_TIME.plusDays(1).toDate()
                ),
                aFirestoreEvent(
                    id = "5",
                    startTime = A_START_TIME.plusDays(1).toDate(),
                    endTime = AN_END_TIME.plusDays(1).toDate()
                )
            )
        )
        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(firstDaySchedulePage, secondDaySchedulePage)))
        val allowedTracks = setOf(aTrack(id = "a track id"))
        `when`(tracksFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(allowedTracks))
        `when`(checksum.getChecksumOf("event_1")).thenReturn(1)
        `when`(checksum.getChecksumOf("event_2")).thenReturn(2)
        `when`(checksum.getChecksumOf("event_3")).thenReturn(3)
        `when`(checksum.getChecksumOf("event_4")).thenReturn(4)
        `when`(checksum.getChecksumOf("event_5")).thenReturn(5)
        `when`(checksum.getChecksumOf("event_6")).thenReturn(6)

        scheduleService.schedule()
            .test()
            .assertValue(
                aSchedule(
                    pages = listOf(
                        aSchedulePage(
                            dayId = "1",
                            date = A_START_TIME.toDateTime(A_TIMEZONE).toLocalDate(),
                            events = listOf(
                                anEvent(
                                    id = "1",
                                    numericId = 1,
                                    startTime = A_START_TIME.toDateTime(A_TIMEZONE).toLocalDateTime(),
                                    endTime = AN_END_TIME.toDateTime(A_TIMEZONE).toLocalDateTime()
                                ),
                                anEvent(
                                    id = "3",
                                    numericId = 3,
                                    startTime = A_START_TIME.toDateTime(A_TIMEZONE).toLocalDateTime(),
                                    endTime = AN_END_TIME.toDateTime(A_TIMEZONE).toLocalDateTime()
                                ),
                                anEvent(
                                    id = "2",
                                    numericId = 2,
                                    startTime = A_LATER_START_TIME.toDateTime(A_TIMEZONE).toLocalDateTime(),
                                    endTime = A_LATER_END_TIME.toDateTime(A_TIMEZONE).toLocalDateTime()
                                )
                            )
                        ),
                        aSchedulePage(
                            dayId = "2",
                            date = A_START_TIME.plusDays(1).toDateTime(A_TIMEZONE).toLocalDate(),
                            events = listOf(
                                anEvent(
                                    id = "4",
                                    numericId = 4,
                                    startTime = A_START_TIME.plusDays(1).toDateTime(A_TIMEZONE).toLocalDateTime(),
                                    endTime = AN_END_TIME.plusDays(1).toDateTime(A_TIMEZONE).toLocalDateTime()
                                ),
                                anEvent(
                                    id = "5",
                                    numericId = 5,
                                    startTime = A_START_TIME.plusDays(1).toDateTime(A_TIMEZONE).toLocalDateTime(),
                                    endTime = AN_END_TIME.plusDays(1).toDateTime(A_TIMEZONE).toLocalDateTime()
                                ),
                                anEvent(
                                    id = "6",
                                    numericId = 6,
                                    startTime = A_LATER_START_TIME.plusDays(1).toDateTime(A_TIMEZONE).toLocalDateTime(),
                                    endTime = A_LATER_END_TIME.plusDays(1).toDateTime(A_TIMEZONE).toLocalDateTime()
                                )
                            )
                        )
                    )
                )
            )
    }

    @Test
    fun `should correctly set favourited flag on events from the schedule when user is signed in`() {
        val schedulePage = aFirestoreSchedulePage(
            events = listOf(
                aFirestoreEvent(id = "1", track = null),
                aFirestoreEvent(id = "2", track = null)
            )
        )
        `when`(dbService.scheduleView()).thenReturn(Observable.just(listOf(schedulePage)))
        val allowedTracks = setOf(aTrack(id = "a track id"))
        `when`(tracksFilter.selectedTracks).thenReturn(BehaviorSubject.createDefault(allowedTracks))
        `when`(checksum.getChecksumOf("event_1")).thenReturn(1)
        `when`(checksum.getChecksumOf("event_2")).thenReturn(2)

        val favorites = listOf(aFirestoreFavorite(id = "2"))
        `when`(dbService.favorites(UID)).thenReturn(Observable.just(favorites))

        scheduleService.schedule()
            .test()
            .assertValue(
                aSchedule(
                    pages = listOf(
                        aSchedulePage(
                            events = listOf(
                                anEvent(id = "1", numericId = 1, track = Option.empty(), favorited = false),
                                anEvent(id = "2", numericId = 2, track = Option.empty(), favorited = true)
                            )
                        )
                    )
                )
            )
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
        `when`(checksum.getChecksumOf("event_${aFirestoreEvent().id}")).thenReturn(1)

        scheduleService.schedule()
            .test()
            .assertValue(
                aSchedule(
                    pages = listOf(
                        aSchedulePage(
                            events = listOf(
                                anEvent(track = Option.empty(), numericId = 1),
                                anEvent(track = Option.empty(), numericId = 1)
                            )
                        )
                    )
                )
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
        `when`(checksum.getChecksumOf("event_${aFirestoreEvent().id}")).thenReturn(1)

        scheduleService.schedule()
            .test()
            .assertValue(
                aSchedule(
                    pages = listOf(
                        aSchedulePage(
                            events = listOf(
                                anEvent(track = Option.empty(), numericId = 1),
                                anEvent(track = Option.empty(), numericId = 1)
                            )
                        )
                    )
                )
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

        scheduleService.schedule()
            .test()
            .assertValue(
                aSchedule(
                    pages = listOf(
                        aSchedulePage(events = emptyList())
                    )
                )
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
        `when`(checksum.getChecksumOf("event_${aFirestoreEvent().id}")).thenReturn(1)

        scheduleService.schedule()
            .test()
            .assertValue(
                aSchedule(
                    pages = listOf(
                        aSchedulePage(
                            events = listOf(
                                anEvent(track = Option(aTrack(id = "A")), numericId = 1),
                                anEvent(track = Option(aTrack(id = "C")), numericId = 1)
                            )
                        )
                    )
                )
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
        `when`(checksum.getChecksumOf("event_${aFirestoreEvent().id}")).thenReturn(1)

        scheduleService.schedule()
            .test()
            .assertValue(
                aSchedule(
                    pages = listOf(
                        aSchedulePage(
                            events = listOf(
                                anEvent(track = Option.empty(), numericId = 1)
                            )
                        )
                    )
                )
            )
    }
}
