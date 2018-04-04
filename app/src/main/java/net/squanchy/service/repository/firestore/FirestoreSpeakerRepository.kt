package net.squanchy.service.repository.firestore

import io.reactivex.Observable
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.firebase.toSpeaker
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.checksum.Checksum

class FirestoreSpeakerRepository(
    private val dbService: FirestoreDbService,
    private val checksum: Checksum
) : SpeakerRepository {

    override fun speakers(): Observable<List<Speaker>> {
        return dbService.speakers()
            .map { it.map { it.toSpeaker(checksum) } }
    }

    override fun speaker(speakerId: String): Observable<Speaker> {
        return dbService.speaker(speakerId)
            .map { it.toSpeaker(checksum) }
    }
}
