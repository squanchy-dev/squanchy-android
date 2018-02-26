package net.squanchy.service.firestore.model.schedule

import io.reactivex.Observable
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.toTrack

interface TrackService {
    fun tracks(): Observable<List<Track>>
}

class FirestoreTrackService(private val firestoreDbService: FirestoreDbService) : TrackService {

    override fun tracks(): Observable<List<Track>> = firestoreDbService.tracks()
        .map { firestoreTracks -> firestoreTracks.map { it.toTrack() } }
}
