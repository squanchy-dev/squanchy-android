package net.squanchy.venue

import dagger.Module
import dagger.Provides
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService

@Module
internal class VenueInfoModule {

    @Provides
    fun favoritesService(venueRepository: FirestoreDbService, authService: FirebaseAuthService) =
            VenueInfoService(venueRepository, authService)
}
