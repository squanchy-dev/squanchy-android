package net.squanchy.service.repository.injection

import dagger.Module
import dagger.Provides
import net.squanchy.service.DaysRepository
import net.squanchy.service.firebase.FirebaseDbService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.FilterScheduleRepository
import net.squanchy.service.repository.LocalFilterScheduleRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.service.repository.firebase.FirebaseDaysRepository
import net.squanchy.service.repository.firebase.FirebaseEventRepository
import net.squanchy.service.repository.firebase.FirestoreSpeakerRepository
import net.squanchy.support.lang.Checksum
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    internal fun eventService(dbService: FirebaseDbService, firestoreDbService: FirestoreDbService, checksum: Checksum): EventRepository {
        return FirebaseEventRepository(dbService, firestoreDbService, checksum)
    }

    @Provides
    internal fun speakerRepository(dbService: FirestoreDbService, checksum: Checksum): SpeakerRepository {
        return FirestoreSpeakerRepository(dbService, checksum)
    }

    @Provides
    internal fun daysRepository(dbService: FirebaseDbService): DaysRepository {
        return FirebaseDaysRepository(dbService)
    }

    @Provides
    @Singleton
    internal fun filterScheduleRepository(): FilterScheduleRepository = LocalFilterScheduleRepository()
}
