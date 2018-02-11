package net.squanchy.service.repository.injection;

import net.squanchy.service.DaysRepository;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firestore.FirestoreDbService;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.service.repository.SpeakerRepository;
import net.squanchy.service.repository.firebase.FirebaseDaysRepository;
import net.squanchy.service.repository.firebase.FirebaseEventRepository;
import net.squanchy.service.repository.firebase.FirestoreSpeakerRepository;
import net.squanchy.support.lang.Checksum;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    EventRepository eventService(FirebaseDbService dbService, FirestoreDbService firestoreDbService, Checksum checksum) {
        return new FirebaseEventRepository(dbService, firestoreDbService, checksum);
    }

    @Provides
    SpeakerRepository speakerRepository(FirestoreDbService dbService, Checksum checksum) {
        return new FirestoreSpeakerRepository(dbService, checksum);
    }

    @Provides
    DaysRepository daysRepository(FirebaseDbService dbService) {
        return new FirebaseDaysRepository(dbService);
    }
}
