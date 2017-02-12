package net.squanchy.speaker;

import net.squanchy.service.firebase.FirebaseSquanchyRepository;

import dagger.Module;
import dagger.Provides;

@Module
class SpeakerModule {

    @Provides
    SpeakerService speakerService(FirebaseSquanchyRepository repository) {
        return new SpeakerService(repository);
    }

}