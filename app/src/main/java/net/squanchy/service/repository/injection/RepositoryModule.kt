package net.squanchy.service.repository.injection

import dagger.Module
import dagger.Provides
import net.squanchy.service.firebase.FirebaseDbService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.repository.DaysRepository
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.service.repository.TracksRepository
import net.squanchy.service.repository.firebase.FirebaseDaysRepository
import net.squanchy.service.repository.firebase.FirebaseEventRepository
import net.squanchy.service.repository.firebase.FirestoreSpeakerRepository
import net.squanchy.service.repository.firestore.FirestoreTracksRepository
import net.squanchy.support.lang.Checksum

@Module
class RepositoryModule {

    @Provides
    internal fun eventRepository(dbService: FirebaseDbService, firestoreDbService: FirestoreDbService, checksum: Checksum): EventRepository =
        FirebaseEventRepository(dbService, firestoreDbService, checksum)

    @Provides
    internal fun speakerRepository(dbService: FirestoreDbService, checksum: Checksum): SpeakerRepository =
        FirestoreSpeakerRepository(dbService, checksum)

    @Provides
    internal fun daysRepository(dbService: FirebaseDbService): DaysRepository = FirebaseDaysRepository(dbService)

    @Provides
    internal fun tracksRepository(dbService: FirestoreDbService): TracksRepository = FirestoreTracksRepository(dbService)
}
