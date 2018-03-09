package net.squanchy.service.repository.firestore

import io.reactivex.Observable
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.toSpeaker
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.lang.Checksum

interface SpeakerRepository {

    fun speakers(): Observable<List<Speaker>>

    fun speaker(speakerId: String): Observable<Speaker>
}

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
