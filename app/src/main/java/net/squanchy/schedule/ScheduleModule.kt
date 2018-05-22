package net.squanchy.schedule

import dagger.Module
import dagger.Provides
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.repository.AuthService
import net.squanchy.support.checksum.ChecksumModule
import net.squanchy.support.checksum.Checksum

@Module(includes = [ChecksumModule::class])
class ScheduleModule {

    @Provides
    internal fun scheduleService(
        authService: AuthService,
        dbService: FirestoreDbService,
        tracksFilter: TracksFilter,
        checksum: Checksum
    ): ScheduleService = FirestoreScheduleService(authService, dbService, tracksFilter, checksum)
}
