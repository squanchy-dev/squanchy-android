package net.squanchy.service.repository.firestore

import io.reactivex.Observable
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.schedule.FirestoreTrack
import net.squanchy.service.firestore.toTrack
import net.squanchy.service.repository.TracksRepository

class FirestoreTracksRepository(private val firestoreDbService: FirestoreDbService) : TracksRepository {

    override fun tracks(): Observable<List<Track>> = firestoreDbService.tracks()
        .map { firestoreTracks -> firestoreTracks.map(FirestoreTrack::toTrack) }
}
