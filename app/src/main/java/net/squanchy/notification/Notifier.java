package net.squanchy.notification;

import android.app.Notification;
import android.support.v4.app.NotificationManagerCompat;

import java.util.List;

public final class Notifier {

    private static final int SINGLE_NOTIFICATION_ID = 42;

    private final NotificationManagerCompat notificationManagerCompat;

    public Notifier(NotificationManagerCompat notificationManagerCompat) {
        this.notificationManagerCompat = notificationManagerCompat;
    }

    public void showNotifications(List<Notification> notifications) {
        notificationManagerCompat.cancelAll();

        int notificationsSize = notifications.size();
        for (int i = 0; i < notificationsSize; i++) {
            Notification notification = notifications.get(i);
            int notificationId = SINGLE_NOTIFICATION_ID + i;
            showNotification(notification, notificationId);
        }
    }

    private void showNotification(Notification singleNotification, int notificationId) {
        notificationManagerCompat.notify(notificationId, singleNotification);
    }
}
