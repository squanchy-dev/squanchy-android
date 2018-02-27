package net.squanchy.schedule.filterschedule

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.squanchy.schedule.domain.view.Track

interface TracksFilter {

    fun updateSelectedTracks(newSelectedTracks: Set<Track>)

    val selectedTracks: Observable<Set<Track>>
}

class InMemoryTracksFilter : TracksFilter {

    private val selectedTracksSubject = BehaviorSubject.createDefault(emptySet<Track>())

    override fun updateSelectedTracks(newSelectedTracks: Set<Track>) {
        selectedTracksSubject.onNext(newSelectedTracks)
    }

    override val selectedTracks: Observable<Set<Track>>
        get() = selectedTracksSubject
}
