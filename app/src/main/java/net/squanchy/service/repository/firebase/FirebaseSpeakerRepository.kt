package net.squanchy.service.repository.firebase

import net.squanchy.service.firebase.FirebaseDbService
import net.squanchy.service.firebase.model.FirebaseSpeaker
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.lang.Checksum
import net.squanchy.support.lang.Optional

import io.reactivex.Observable

import net.squanchy.support.lang.Lists.map

class FirebaseSpeakerRepository(
        private val dbService: FirebaseDbService,
        private val checksum: Checksum
) : SpeakerRepository {

    override fun speakers(): Observable<List<Speaker>> {
        return dbService.speakers()
            .map { firebaseSpeaker -> firebaseSpeaker.speakers }
            .map { speakers -> map<FirebaseSpeaker, Speaker>(speakers, { toSpeaker(it) }) }
    }

    override fun speaker(speakerId: String): Observable<Speaker> {
        return dbService.speakers()
            .map{ firebaseSpeakers -> firebaseSpeakers.speakers }
            .flatMap { Observable.fromIterable(it) }
            .filter { (id) -> id == speakerId }
            .map { toSpeaker(it) }
    }

    private fun toSpeaker(firebaseSpeaker: FirebaseSpeaker): Speaker {
        return Speaker.create(
                firebaseSpeaker.id!!,
                checksum.getChecksumOf(firebaseSpeaker.id!!),
                firebaseSpeaker.name!!,
                firebaseSpeaker.bio!!,
                Optional.fromNullable(firebaseSpeaker.company_name),
                Optional.fromNullable(firebaseSpeaker.company_url),
                Optional.fromNullable(firebaseSpeaker.personal_url),
                Optional.fromNullable(firebaseSpeaker.photo_url),
                Optional.fromNullable(firebaseSpeaker.twitter_username)
        )
    }
}
