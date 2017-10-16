package net.squanchy.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import timber.log.Timber

class NotificationAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED != intent.action) {
            Timber.d("Intercepted spoofed intent directed to NotificationAlarmReceiver: %s", intent)
            return
        }

        val serviceIntent = Intent(context, NotificationsIntentService::class.java)
        context.startService(serviceIntent)
    }
}
