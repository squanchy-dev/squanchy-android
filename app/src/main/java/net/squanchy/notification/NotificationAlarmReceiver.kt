package net.squanchy.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.threeten.bp.LocalDateTime

import timber.log.Timber

class NotificationAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED != intent.action) {
            Timber.d("Intercepted spoofed intent directed to NotificationAlarmReceiver: $intent")
            return
        }

        scheduleNotificationWork(LocalDateTime.now())
    }
}
