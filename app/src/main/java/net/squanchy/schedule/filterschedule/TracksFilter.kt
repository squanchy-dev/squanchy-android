package net.squanchy.schedule.filterschedule

import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import net.squanchy.schedule.domain.view.Track

interface TracksFilter {

    fun updateSelectedTracks(newSelectedTracks: Set<Track>)

    val selectedTracks: Observable<Set<Track>>

    fun isInitialized(): Boolean
}

class InMemoryTracksFilter : TracksFilter {

    private val selectedTracksSubject = ReplaySubject.createWithSize<Set<Track>>(1)

    private var initialized = false

    override fun isInitialized() = initialized

    override fun updateSelectedTracks(newSelectedTracks: Set<Track>) {
        initialized = true
        selectedTracksSubject.onNext(newSelectedTracks)
    }

    override val selectedTracks: Observable<Set<Track>>
        get() = selectedTracksSubject
}
