package net.squanchy.notification

import android.app.Notification
import androidx.core.app.NotificationManagerCompat

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

    companion object {
        private const val SINGLE_NOTIFICATION_ID = 42
    }
}
