package net.squanchy.speaker;

import net.squanchy.service.firebase.FirebaseSquanchyRepository;

import dagger.Module;
import dagger.Provides;

@Module
class SpeakerModule {

    private final SpeakerListActivity activity;

    SpeakerModule(SpeakerListActivity activity) {
        this.activity = activity;
    }

    @Provides
    SpeakerService speakerService(FirebaseSquanchyRepository repository) {
        return new SpeakerService(repository);
    }

    @Provides
    SpeakerNavigator scheduleNavigator() {
        return new SpeakerNavigator(activity);
    }
}