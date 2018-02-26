package net.squanchy.service.repository

import io.reactivex.observers.TestObserver
import net.squanchy.schedule.domain.view.Track
import net.squanchy.support.lang.Optional
import org.junit.Test

class LocalTrackFilterTest {

    private val trackFilterRepository = LocalTrackFilter

    companion object {
        val A_SET_OF_TRACKS = setOf(Track("any", "name", Optional.absent(), Optional.absent(), Optional.absent()))
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
