package net.squanchy.service.repository.firestore

import io.reactivex.Observable
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.toTrack

interface TracksRepository {

    fun tracks(): Observable<List<Track>>
}

class FirestoreTracksRepository(private val firestoreDbService: FirestoreDbService) : TracksRepository {

    override fun tracks(): Observable<List<Track>> = firestoreDbService.tracks()
        .map { firestoreTracks -> firestoreTracks.map { it.toTrack() } }
}
