package net.squanchy.speaker

import io.reactivex.Observable
import net.squanchy.service.repository.AuthService
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.speaker.domain.view.Speaker

internal class SpeakerDetailsService(
    private val speakerRepository: SpeakerRepository,
    private val authService: AuthService
) {
    fun speaker(speakerId: String): Observable<Speaker> =
        authService.ifUserSignedInThenObservableFrom { speakerRepository.speaker(speakerId) }
}
