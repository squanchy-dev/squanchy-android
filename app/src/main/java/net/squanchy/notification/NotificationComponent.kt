package net.squanchy.notification

import android.content.Context
import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent

internal fun notificationComponent(context: Context): NotificationComponent =
    DaggerNotificationComponent.builder()
        .applicationComponent(context.applicationComponent)
        .build()

@ActivityLifecycle
@Component(modules = [NotificationModule::class], dependencies = [ApplicationComponent::class])
internal interface NotificationComponent {

    fun service(): NotificationService

    fun notificationCreator(): NotificationCreator

    fun notifier(): Notifier
}
