package net.squanchy.notification

import android.app.Service
import android.content.Context
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.ServiceContextModule
import net.squanchy.injection.applicationComponent

internal fun notificationComponent(service: Service): NotificationComponent {
    return DaggerNotificationComponent.builder()
            .applicationComponent(service.applicationComponent)
            .serviceContextModule(ServiceContextModule(service))
            .build()
}

@ActivityLifecycle
@Component(modules = arrayOf(NotificationModule::class), dependencies = arrayOf(ApplicationComponent::class))
internal interface NotificationComponent {

    fun service(): NotificationService

    fun notificationCreator(): NotificationCreator

    fun notifier(): Notifier

    fun context(): Context

    fun analytics(): Analytics
}
