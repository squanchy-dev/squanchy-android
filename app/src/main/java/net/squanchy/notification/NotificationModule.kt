package net.squanchy.notification

import android.content.Context
import android.support.v4.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides
import net.squanchy.injection.ServiceContextModule
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.repository.EventRepository

@Module(includes = arrayOf(ServiceContextModule::class))
internal class NotificationModule {

    @Provides
    fun favoritesService(
            authService: FirebaseAuthService,
            eventRepository: EventRepository
    ): NotificationService {
        return NotificationService(authService, eventRepository)
    }

    @Provides
    fun notificationCreator(context: Context): NotificationCreator {
        return NotificationCreator(context)
    }

    @Provides
    fun notificationManagerCompat(context: Context): NotificationManagerCompat {
        return NotificationManagerCompat.from(context)
    }

    @Provides
    fun notifier(notificationManagerCompat: NotificationManagerCompat): Notifier {
        return Notifier(notificationManagerCompat)
    }
}
