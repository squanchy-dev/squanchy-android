package net.squanchy.schedule.filterschedule

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.squanchy.schedule.domain.view.Track

interface TracksFilter {

    fun updateSelectedTracks(newSelectedTracks: Set<Track>)

    val selectedTracks: Observable<Set<Track>>

    val isInitialized: Boolean
}

class InMemoryTracksFilter : TracksFilter {

    private val selectedTracksSubject = BehaviorSubject.create<Set<Track>>()

    private var initialized = false

    override val isInitialized
        get() = initialized

    override fun updateSelectedTracks(newSelectedTracks: Set<Track>) {
        initialized = true
        selectedTracksSubject.onNext(newSelectedTracks)
    }

    override val selectedTracks: Observable<Set<Track>>
        get() = selectedTracksSubject
}
