package net.squanchy.favorites;

import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.EventRepository;

import dagger.Module;
import dagger.Provides;

@Module
class FavoritesModule {

    @Provides
    FavoritesService favoritesService(
            FirebaseAuthService authService,
            EventRepository eventRepository
    ) {
        return new FavoritesService(authService, eventRepository);
    }
}
