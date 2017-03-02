package net.squanchy.speaker;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;
import net.squanchy.support.lang.Checksum;

import dagger.Module;
import dagger.Provides;

@Module
class SpeakerModule {

    @Provides
    SpeakerService speakerService(@DbServiceType(DbServiceType.Type.AUTHENTICATED) FirebaseDbService dbService, Checksum checksum) {
        return new SpeakerService(dbService, checksum);
    }
}
