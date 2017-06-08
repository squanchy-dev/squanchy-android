package net.squanchy.speaker;

import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.SpeakerRepository;

import dagger.Module;
import dagger.Provides;

@Module
class SpeakerDetailsModule {

    @Provides
    SpeakerDetailsService scheduleService(SpeakerRepository speakerRepository, FirebaseAuthService authService) {
        return new SpeakerDetailsService(speakerRepository, authService);
    }
}
