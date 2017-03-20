package net.squanchy.notification;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import net.squanchy.injection.ServiceContextModule;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.EventRepository;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ServiceContextModule.class})
class NotificationModule {

    @Provides
    NotificationService favoritesService(
            FirebaseAuthService authService,
            EventRepository eventRepository
    ) {
        return new NotificationService(authService, eventRepository);
    }

    @Provides
    NotificationCreator notificationCreator(Context context) {
        return new NotificationCreator(context);
    }

    @Provides
    NotificationManagerCompat notificationManagerCompat(Context context) {
        return NotificationManagerCompat.from(context);
    }

    @Provides
    Notifier notifier(NotificationManagerCompat notificationManagerCompat) {
        return new Notifier(notificationManagerCompat);
    }
}
