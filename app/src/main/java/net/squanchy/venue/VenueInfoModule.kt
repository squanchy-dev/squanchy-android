package net.squanchy.venue

import dagger.Module
import dagger.Provides
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.repository.VenueRepository

@Module
internal class VenueInfoModule {

    @Provides
    fun favoritesService(venueRepository: VenueRepository, authService: FirebaseAuthService) =
            VenueInfoService(venueRepository, authService)
}
