package net.squanchy.notification;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import it.near.sdk.recipes.background.NearItBroadcastReceiver;

public class NearNotificationReceiver extends NearItBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                NearNotificationIntentService.class.getName());

        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, intent.setComponent(comp));
    }
}