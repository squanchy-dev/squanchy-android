package net.squanchy.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Timber.d("Intercepted spoofed intent directed to NotificationAlarmReceiver: %s", intent);
            return;
        }

        Intent serviceIntent = new Intent(context, NotificationsIntentService.class);
        context.startService(serviceIntent);
    }
}
