package net.squanchy.schedule.tracksfilter

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.repository.TracksRepository

interface TracksFilter {

    fun updateSelectedTracks(newSelectedTracks: Set<Track>)

    val selectedTracks: Observable<Set<Track>>
}

class InMemoryTracksFilter(private val tracksRepository: TracksRepository) : TracksFilter {

    init {
        println("!!! I AM ALIVE \uD83D\uDC76 ${hashCode()}")
    }

    private val selectedTracksSubject = BehaviorSubject.create<Set<Track>>()

    override fun updateSelectedTracks(newSelectedTracks: Set<Track>) {
        selectedTracksSubject.onNext(newSelectedTracks)
    }

    override val selectedTracks: Observable<Set<Track>>
        get() = selectedTracksSubject.orStartWithDbIfEmpty()

    private fun BehaviorSubject<Set<Track>>.orStartWithDbIfEmpty() =
        if (hasValue()) this else this.startWith(tracksRepository.tracks().take(1).asSets())

    private fun <T> Observable<List<T>>.asSets(): Observable<Set<T>> = map { it -> it.toSet() }
}
