package net.squanchy.speaker

import net.squanchy.service.firestore.FirebaseAuthService

import dagger.Module
import dagger.Provides

@Module
internal class SpeakerDetailsModule {

    @Provides
    fun scheduleService(speakerRepository: SpeakerRepository, authService: FirebaseAuthService) =
        SpeakerDetailsService(speakerRepository, authService)
}
