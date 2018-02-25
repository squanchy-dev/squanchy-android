package net.squanchy

import io.reactivex.subjects.BehaviorSubject
import net.squanchy.schedule.filterschedule.FilterScheduleService
import net.squanchy.service.firestore.aFirestoreTrack
import net.squanchy.service.firestore.toTrack
import net.squanchy.service.repository.FilterScheduleRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class FilterScheduleServiceTest {

    @Rule
    @JvmField
    var rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var filterRepository: FilterScheduleRepository

    lateinit var filterScheduleService: FilterScheduleService

    @Test
    fun `filter should have all tracks as selected when it is empty`() {
        `when`(filterRepository.filters).thenReturn(BehaviorSubject.createDefault(emptySet()))
        val allTracks = listOf(aFirestoreTrack().toTrack())
        `when`(filterRepository.allTracks).thenReturn(allTracks)

        filterScheduleService = FilterScheduleService(filterRepository)

        filterScheduleService.currentSelection.forEach { assertTrue(it) }
    }

    @Test
    fun `current selection should contains true value only when the track filter is selected`() {
        val trackA = aFirestoreTrack(id = "a").toTrack()
        val trackB = aFirestoreTrack(id = "b").toTrack()
        val tracks = listOf(trackA, trackB)
        val filteredTrackIndex = 1
        `when`(filterRepository.filters).thenReturn(BehaviorSubject.createDefault(setOf(tracks[filteredTrackIndex])))
        `when`(filterRepository.allTracks).thenReturn(tracks)

        filterScheduleService = FilterScheduleService(filterRepository)

        assertTrue(filterScheduleService.currentSelection[filteredTrackIndex])
        assertFalse(filterScheduleService.currentSelection[0])
    }
}
