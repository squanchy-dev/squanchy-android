package net.squanchy.service.repository.injection;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.service.repository.SpeakerRepository;
import net.squanchy.support.lang.Checksum;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    EventRepository eventService(
            @DbServiceType(DbServiceType.Type.AUTHENTICATED) FirebaseDbService dbService,
            Checksum checksum,
            SpeakerRepository speakerRepository
    ) {
        return new EventRepository(dbService, checksum, speakerRepository);
    }

    @Provides
    SpeakerRepository speakerRepository(
            @DbServiceType(DbServiceType.Type.AUTHENTICATED) FirebaseDbService dbService,
            Checksum checksum
    ) {
        return new SpeakerRepository(dbService, checksum);
    }
}
