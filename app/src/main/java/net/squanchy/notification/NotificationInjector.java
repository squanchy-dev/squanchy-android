package net.squanchy.notification;

import android.content.Context;

import net.squanchy.injection.ApplicationInjector;

final class NotificationInjector {

    private NotificationInjector() {
        // no instances
    }

    public static NotificationComponent obtain(Context context) {
        return DaggerNotificationComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(context))
                .build();
    }
}
