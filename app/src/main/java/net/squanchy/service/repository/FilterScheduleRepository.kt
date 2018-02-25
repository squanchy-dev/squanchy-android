package net.squanchy.service.repository

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.toTrack

interface FilterScheduleRepository {

    val filters: BehaviorSubject<Set<Track>>
    val allTracks: Observable<List<Track>>

    fun setFilter(tracks: Set<Track>)
}

class LocalFilterScheduleRepository(dbService: FirestoreDbService) : FilterScheduleRepository {

    override val allTracks = dbService.tracks().map { it.map { it.toTrack() } }
    override val filters: BehaviorSubject<Set<Track>> = BehaviorSubject.createDefault(emptySet())

    override fun setFilter(tracks: Set<Track>) {
        filters.onNext(tracks)
    }
}
