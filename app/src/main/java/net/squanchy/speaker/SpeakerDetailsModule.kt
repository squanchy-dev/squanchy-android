package net.squanchy.speaker

import dagger.Module
import dagger.Provides
import net.squanchy.service.repository.AuthService
import net.squanchy.service.repository.SpeakerRepository

@Module
internal class SpeakerDetailsModule {

    @Provides
    fun scheduleService(speakerRepository: SpeakerRepository, authService: AuthService) =
        SpeakerDetailsService(speakerRepository, authService)
}
