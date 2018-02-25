package net.squanchy.schedule.filterschedule

import dagger.Module
import dagger.Provides
import net.squanchy.service.repository.FilterScheduleRepository

@Module
class FilterScheduleModule {

    @Provides
    internal fun filterScheduleService(filterScheduleRepository: FilterScheduleRepository): FilterScheduleService =
        FilterScheduleService(filterScheduleRepository)
}
