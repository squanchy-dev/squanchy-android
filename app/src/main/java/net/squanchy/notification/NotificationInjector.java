package net.squanchy.notification;

import android.app.Service;

import net.squanchy.injection.ApplicationInjector;
import net.squanchy.injection.ServiceContextModule;

final class NotificationInjector {

    private NotificationInjector() {
        // no instances
    }

    public static NotificationComponent obtain(Service service) {
        return DaggerNotificationComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(service))
                .serviceContextModule( new ServiceContextModule(service))
                .build();
    }
}
