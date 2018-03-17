package net.squanchy.service.repository.injection

import dagger.Module
import dagger.Provides
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.firestore.FirestoreEventRepository
import net.squanchy.service.repository.firestore.FirestoreSpeakerRepository
import net.squanchy.service.repository.firestore.FirestoreTracksRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.service.repository.TracksRepository
import net.squanchy.support.lang.Checksum

@Module
class RepositoryModule {

    @Provides
    internal fun eventRepository(firestoreDbService: FirestoreDbService, checksum: Checksum): EventRepository =
        FirestoreEventRepository(firestoreDbService, checksum)

    @Provides
    internal fun speakerRepository(dbService: FirestoreDbService, checksum: Checksum): SpeakerRepository =
        FirestoreSpeakerRepository(dbService, checksum)

    @Provides
    internal fun tracksRepository(dbService: FirestoreDbService): TracksRepository =
        FirestoreTracksRepository(dbService)
}
