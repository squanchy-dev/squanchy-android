package net.squanchy.service.repository.injection

import dagger.Module
import dagger.Provides
import net.squanchy.schedule.TracksRepository
import net.squanchy.schedule.firestore.FirestoreTracksRepository
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.service.repository.firestore.FirestoreEventRepository
import net.squanchy.service.repository.firestore.FirestoreSpeakerRepository
import net.squanchy.support.checksum.Checksum
import net.squanchy.support.checksum.ChecksumModule

@Module(includes = [ChecksumModule::class])
class RepositoryModule {

    @Provides
    internal fun eventRepository(firestoreDbService: FirestoreDbService, checksum: Checksum): EventRepository =
        FirestoreEventRepository(firestoreDbService, checksum)

    @Provides
    internal fun speakerRepository(dbService: FirestoreDbService, checksum: Checksum): SpeakerRepository =
        FirestoreSpeakerRepository(dbService, checksum)

    @Provides
    internal fun tracksRepository(dbService: FirestoreDbService, checksum: Checksum): TracksRepository =
        FirestoreTracksRepository(dbService, checksum)
}
