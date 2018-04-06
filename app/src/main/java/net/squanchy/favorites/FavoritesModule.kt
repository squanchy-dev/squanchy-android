package net.squanchy.favorites

import dagger.Module
import dagger.Provides
import net.squanchy.schedule.ScheduleModule
import net.squanchy.schedule.ScheduleService
import net.squanchy.service.repository.AuthService

@Module(includes = [ScheduleModule::class])
class FavoritesModule {

    @Provides
    internal fun favoritesService(
        authService: AuthService,
        scheduleService: ScheduleService
    ): FavoritesService = FirestoreFavoritesService(authService, scheduleService)
}
