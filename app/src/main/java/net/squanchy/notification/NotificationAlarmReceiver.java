package net.squanchy.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, NotificationsIntentService.class);
        context.startService(serviceIntent);
    }
}
