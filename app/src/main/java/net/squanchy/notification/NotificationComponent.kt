package net.squanchy.notification

import android.content.Context
import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.notification.NotificationModule.Companion.NOTIFICATION_INTERVAL
import net.squanchy.support.injection.CurrentTimeModule
import net.squanchy.support.system.CurrentTime
import org.threeten.bp.Duration
import javax.inject.Named

internal fun notificationComponent(context: Context): NotificationComponent =
    DaggerNotificationComponent.builder()
        .applicationComponent(context.applicationComponent)
        .build()

@ActivityLifecycle
@Component(modules = [NotificationModule::class, CurrentTimeModule::class], dependencies = [ApplicationComponent::class])
internal interface NotificationComponent {

    fun notificationCreator(): NotificationCreator

    fun notifier(): Notifier

    fun currentTime(): CurrentTime

    fun upcomingEventsService(): UpcomingEventsService

    @Named(NOTIFICATION_INTERVAL)
    fun notificationInterval(): Duration
}
