package net.squanchy.notification;

import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.EventRepository;

import dagger.Module;
import dagger.Provides;

@Module
class NotificationModule {

    @Provides
    NotificationService favoritesService(
            FirebaseAuthService authService,
            EventRepository eventRepository
    ) {
        return new NotificationService(authService, eventRepository);
    }
}
