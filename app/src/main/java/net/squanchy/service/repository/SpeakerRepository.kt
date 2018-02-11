package net.squanchy.service.repository

import net.squanchy.speaker.domain.view.Speaker

import io.reactivex.Observable

interface SpeakerRepository {

    fun speakers(): Observable<List<Speaker>>

    fun speaker(speakerId: String): Observable<Speaker>
}
