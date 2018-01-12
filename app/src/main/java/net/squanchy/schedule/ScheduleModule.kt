package net.squanchy.schedule

import dagger.Module
import dagger.Provides
import net.squanchy.service.DaysRepository
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.repository.EventRepository

@Module
class ScheduleModule {

    @Provides
    internal fun scheduleService(
            authService: FirebaseAuthService,
            eventRepository: EventRepository,
            daysRepository: DaysRepository
    ): ScheduleService = ScheduleService(authService, eventRepository, daysRepository)
}
