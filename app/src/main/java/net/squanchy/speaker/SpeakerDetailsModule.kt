package net.squanchy.speaker

import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.repository.SpeakerRepository

import dagger.Module
import dagger.Provides

@Module
internal class SpeakerDetailsModule {

    @Provides
    fun scheduleService(speakerRepository: SpeakerRepository, authService: FirebaseAuthService) =
        SpeakerDetailsService(speakerRepository, authService)
}
