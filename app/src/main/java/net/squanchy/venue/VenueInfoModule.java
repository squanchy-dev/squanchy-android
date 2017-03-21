package net.squanchy.venue;

import net.squanchy.service.repository.VenueRepository;

import dagger.Module;
import dagger.Provides;

@Module
class VenueInfoModule {

    @Provides
    VenueInfoService favoritesService(
            VenueRepository venueRepository
    ) {
        return new VenueInfoService(venueRepository);
    }
}
