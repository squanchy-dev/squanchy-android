package net.squanchy.venue;

import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.VenueRepository;

import dagger.Module;
import dagger.Provides;

@Module
class VenueInfoModule {

    @Provides
    VenueInfoService favoritesService(VenueRepository venueRepository, FirebaseAuthService authService) {
        return new VenueInfoService(venueRepository, authService);
    }
}
