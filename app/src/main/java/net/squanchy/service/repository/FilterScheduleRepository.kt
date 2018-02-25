package net.squanchy.service.repository

import io.reactivex.subjects.BehaviorSubject
import net.squanchy.schedule.domain.view.Track

interface FilterScheduleRepository {

    val filters: BehaviorSubject<Set<Track>>
    var allTracks: List<Track>

    fun setFilter(tracks: Set<Track>)
}

class LocalFilterScheduleRepository : FilterScheduleRepository {

    override var allTracks: List<Track> = emptyList()
    override val filters: BehaviorSubject<Set<Track>> = BehaviorSubject.createDefault(emptySet())

    override fun setFilter(tracks: Set<Track>) {
        filters.onNext(tracks)
    }
}
