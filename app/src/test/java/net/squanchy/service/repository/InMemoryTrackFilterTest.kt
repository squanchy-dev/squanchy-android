package net.squanchy.service.repository

import io.reactivex.observers.TestObserver
import net.squanchy.schedule.domain.view.Track
import net.squanchy.schedule.domain.view.aTrack
import org.junit.Test

class InMemoryTrackFilterTest {

    private val trackFilterRepository = InMemoryTrackFilter()

    companion object {
        val A_SET_OF_TRACKS = setOf(aTrack())
    }

    @Test
    fun `should start with a default empty set`() {
        val subscription = TestObserver<Set<Track>>()
        trackFilterRepository.selectedTracks
            .subscribe(subscription)

        subscription.assertValue(emptySet())
    }

    @Test
    fun `should emit new selected tracks when they are updated`() {
        val subscription = TestObserver<Set<Track>>()
        trackFilterRepository.selectedTracks
            .subscribe(subscription)

        trackFilterRepository.updateSelectedTracks(A_SET_OF_TRACKS)

        subscription.assertValues(emptySet(), A_SET_OF_TRACKS)
    }

    @Test
    fun `should not terminate when the selected tracks are updated`() {
        val subscription = TestObserver<Set<Track>>()
        trackFilterRepository.selectedTracks
            .subscribe(subscription)

        trackFilterRepository.updateSelectedTracks(A_SET_OF_TRACKS)

        subscription.assertNotTerminated()
    }
}
