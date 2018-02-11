package net.squanchy.service.repository.firebase

import io.reactivex.Observable
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.model.schedule.FirestoreSpeaker
import net.squanchy.service.firestore.toSpeaker
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.lang.Checksum
import net.squanchy.support.lang.Optional

class FirestoreSpeakerRepository(
    private val dbService: FirestoreDbService,
    private val checksum: Checksum
) : SpeakerRepository {

    override fun speakers(): Observable<List<Speaker>> {
        return dbService.speakers()
            .map { it.map { it.toSpeaker(checksum) } }
    }

    override fun speaker(speakerId: String): Observable<Speaker> {
        return dbService.speakers()
            .map { it.first { it.id == speakerId } }
            .map { it.toSpeaker(checksum) }
    }
}
