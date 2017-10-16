package net.squanchy.notification

import android.app.Service

import net.squanchy.injection.ApplicationInjector
import net.squanchy.injection.ServiceContextModule

internal object NotificationInjector {

    fun obtain(service: Service): NotificationComponent {
        return DaggerNotificationComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(service))
                .serviceContextModule(ServiceContextModule(service))
                .build()
    }
}
