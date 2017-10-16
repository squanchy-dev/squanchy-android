package net.squanchy.schedule

import dagger.Module
import dagger.Provides
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firebase.FirebaseDbService
import net.squanchy.service.repository.EventRepository

@Module
class ScheduleModule {

    @Provides
    internal fun scheduleService(
            dbService: FirebaseDbService,
            authService: FirebaseAuthService,
            eventRepository: EventRepository
    ): ScheduleService = ScheduleService(dbService, authService, eventRepository)
}
