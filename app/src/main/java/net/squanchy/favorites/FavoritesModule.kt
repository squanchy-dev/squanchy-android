package net.squanchy.favorites

import dagger.Module
import dagger.Provides
import net.squanchy.schedule.ScheduleModule
import net.squanchy.schedule.ScheduleService
import net.squanchy.service.firebase.FirebaseAuthService

@Module(includes = [ScheduleModule::class])
class FavoritesModule {

    @Provides
    internal fun favoritesService(
        authService: FirebaseAuthService,
        scheduleService: ScheduleService
    ): FavoritesService = FirestoreFavoritesService(authService, scheduleService)
}
