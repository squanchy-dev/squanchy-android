package net.squanchy.speaker;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;

import dagger.Module;
import dagger.Provides;

@Module
class SpeakerModule {

    @Provides
    SpeakerService speakerService(@DbServiceType(DbServiceType.Type.AUTHENTICATED) FirebaseDbService dbService) {
        return new SpeakerService(dbService);
    }
}
