package net.squanchy.service.repository.injection;

import net.squanchy.service.DaysRepository;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.service.repository.SpeakerRepository;
import net.squanchy.service.repository.VenueRepository;
import net.squanchy.service.repository.firebase.FirebaseDaysRepository;
import net.squanchy.service.repository.firebase.FirebaseEventRepository;
import net.squanchy.service.repository.firebase.FirebaseSpeakerRepository;
import net.squanchy.service.repository.firebase.FirebaseVenueRepository;
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
        return new FirebaseEventRepository(dbService, checksum, speakerRepository);
    }

    @Provides
    SpeakerRepository speakerRepository(
            FirebaseDbService dbService,
            Checksum checksum
    ) {
        return new FirebaseSpeakerRepository(dbService, checksum);
    }

    @Provides
    VenueRepository venueRepositoryrRepository(FirebaseDbService dbService) {
        return new FirebaseVenueRepository(dbService);
    }

    @Provides
    DaysRepository daysRepository(FirebaseDbService dbService) {
        return new FirebaseDaysRepository(dbService);
    }
}
