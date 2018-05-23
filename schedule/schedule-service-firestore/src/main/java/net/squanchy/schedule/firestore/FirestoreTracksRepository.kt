package net.squanchy.schedule.firestore

import io.reactivex.Observable
import net.squanchy.schedule.TracksRepository
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.support.checksum.Checksum

class FirestoreTracksRepository(private val firestoreDbService: FirestoreDbService, private val checksum: Checksum) : TracksRepository {

    override fun tracks(): Observable<List<Track>> = firestoreDbService.tracks()
        .map { firestoreTracks -> firestoreTracks.map { it.toTrack(checksum) } }
}
