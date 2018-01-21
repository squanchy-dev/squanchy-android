package net.squanchy.venue

import dagger.Module
import dagger.Provides
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService

@Module
internal class VenueInfoModule {

    @Provides
    fun favoritesService(dbService: FirestoreDbService, authService: FirebaseAuthService) =
            VenueInfoService(dbService, authService)
}
