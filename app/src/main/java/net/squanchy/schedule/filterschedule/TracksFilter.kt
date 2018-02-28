package net.squanchy.schedule.filterschedule

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.repository.TracksRepository

interface TracksFilter {

    fun updateSelectedTracks(newSelectedTracks: Set<Track>)

    val selectedTracks: Observable<Set<Track>>

    val isInitialized: Boolean
}

private val selectedTracksSubject = BehaviorSubject.create<Set<Track>>()

class InMemoryTracksFilter(private val tracksRepository: TracksRepository) : TracksFilter {

    private var initialized = false

    override val isInitialized
        get() = initialized

    override fun updateSelectedTracks(newSelectedTracks: Set<Track>) {
        initialized = true
        selectedTracksSubject.onNext(newSelectedTracks)
    }

    override val selectedTracks: Observable<Set<Track>>
        get() = selectedTracksSubject.startWith(
            tracksRepository.tracks()
                .take(if (selectedTracksSubject.hasValue()) 0 else 1)
                .map { it.toSet() }
        )
}
