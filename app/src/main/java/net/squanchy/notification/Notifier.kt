package net.squanchy.notification

import android.app.Notification
import android.support.v4.app.NotificationManagerCompat

class Notifier(private val notificationManagerCompat: NotificationManagerCompat) {

    fun showNotifications(notifications: List<Notification>) {
        notificationManagerCompat.cancelAll()

        notifications.forEachIndexed { index, notification ->
            val notificationId = SINGLE_NOTIFICATION_ID + index
            showNotification(notification, notificationId)
        }
    }

    private fun showNotification(singleNotification: Notification, notificationId: Int) {
        notificationManagerCompat.notify(notificationId, singleNotification)
    }
}

private val SINGLE_NOTIFICATION_ID = 42
