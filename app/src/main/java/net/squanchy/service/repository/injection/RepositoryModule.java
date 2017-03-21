package net.squanchy.service.repository.injection;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.service.repository.SpeakerRepository;
import net.squanchy.service.repository.VenueRepository;
import net.squanchy.support.lang.Checksum;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    EventRepository eventService(
            FirebaseDbService dbService,
            Checksum checksum,
            SpeakerRepository speakerRepository
    ) {
        return new EventRepository(dbService, checksum, speakerRepository);
    }

    @Provides
    SpeakerRepository speakerRepository(
            FirebaseDbService dbService,
            Checksum checksum
    ) {
        return new SpeakerRepository(dbService, checksum);
    }

    @Provides
    VenueRepository venueRepositoryrRepository(
            FirebaseDbService dbService
    ) {
        return new VenueRepository(dbService);
    }
}
