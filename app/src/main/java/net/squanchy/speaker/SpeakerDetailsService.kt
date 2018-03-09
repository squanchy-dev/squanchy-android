package net.squanchy.speaker

import io.reactivex.Observable
import net.squanchy.service.firestore.FirebaseAuthService
import net.squanchy.service.repository.firestore.SpeakerRepository
import net.squanchy.speaker.domain.view.Speaker

internal class SpeakerDetailsService(
    private val speakerRepository: SpeakerRepository,
    private val authService: FirebaseAuthService
) {
    fun speaker(speakerId: String): Observable<Speaker> =
        authService.ifUserSignedInThenObservableFrom { speakerRepository.speaker(speakerId) }
}
