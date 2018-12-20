package net.squanchy.notification

import android.app.Application
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides
import net.squanchy.service.repository.AuthService
import net.squanchy.service.repository.EventRepository
import net.squanchy.support.system.CurrentTime
import org.threeten.bp.Duration
import javax.inject.Named

@Module
internal class NotificationModule {

    @Provides
    fun context(application: Application): Context = application

    @Provides
    fun favoritesService(authService: AuthService, eventRepository: EventRepository): NotificationService {
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

    @Provides
    @Named(NOTIFICATION_INTERVAL)
    fun provideNotificationInterval(): Duration = Duration.ofMinutes(10)

    @Provides
    fun upcomingEventsService(
        notificationService: NotificationService,
        currentTime: CurrentTime,
        @Named(NOTIFICATION_INTERVAL) notificationInterval: Duration
    ): UpcomingEventsService {
        return UpcomingEventsService(
            notificationService,
            currentTime,
            notificationInterval
        )
    }

    companion object {
        const val NOTIFICATION_INTERVAL = "NOTIFICATION_INTERVAL"
    }
}
