package net.squanchy.schedule

import dagger.Module
import dagger.Provides
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.repository.FilterScheduleRepository
import net.squanchy.support.injection.ChecksumModule
import net.squanchy.support.lang.Checksum

@Module(includes = [ChecksumModule::class])
class ScheduleModule {

    @Provides
    internal fun scheduleService(
        authService: FirebaseAuthService,
        dbService: FirestoreDbService,
        filterScheduleRepository: FilterScheduleRepository,
        checksum: Checksum
    ): ScheduleService = FirestoreScheduleService(authService, dbService, filterScheduleRepository, checksum)
}
