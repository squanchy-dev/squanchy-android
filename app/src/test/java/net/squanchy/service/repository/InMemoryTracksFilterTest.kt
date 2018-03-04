package net.squanchy.service.repository

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import net.squanchy.schedule.domain.view.Track
import net.squanchy.schedule.domain.view.aTrack
import net.squanchy.schedule.tracksfilter.InMemoryTracksFilter
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class InMemoryTracksFilterTest {

    companion object {
        private val ALL_TRACKS = listOf(aTrack(id = "1"), aTrack(id = "2"), aTrack(id = "3"))
        private val A_SUBSET_OF_TRACKS = setOf(ALL_TRACKS[0], ALL_TRACKS[2])
    }

    private val tracksRepository: TracksRepository = Mockito.mock(TracksRepository::class.java)
    private val tracksFilter = InMemoryTracksFilter(tracksRepository)

    @Before
    fun before() {
        `when`(tracksRepository.tracks()).thenReturn(Observable.just(ALL_TRACKS))
    }

    @Test
    fun `should emit all tracks from the repository when first subscribed to`() {
        val subscription = TestObserver<Set<Track>>()
        tracksFilter.selectedTracks
            .subscribe(subscription)

        subscription.assertValue(ALL_TRACKS.toSet())
    }

    @Test
    fun `should emit new selected tracks when they are updated`() {
        val subscription = TestObserver<Set<Track>>()
        tracksFilter.selectedTracks
            .subscribe(subscription)

        tracksFilter.updateSelectedTracks(A_SUBSET_OF_TRACKS)

        subscription.assertValues(ALL_TRACKS.toSet(), A_SUBSET_OF_TRACKS)
    }

    @Test
    fun `should not terminate when the selected tracks are updated`() {
        val subscription = TestObserver<Set<Track>>()
        tracksFilter.selectedTracks
            .subscribe(subscription)

        tracksFilter.updateSelectedTracks(A_SUBSET_OF_TRACKS)

        subscription.assertNotTerminated()
    }
}
