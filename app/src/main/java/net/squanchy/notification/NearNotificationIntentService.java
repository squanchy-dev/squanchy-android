package net.squanchy.notification;

import android.app.Notification;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.analytics.Analytics;

import it.near.sdk.utils.NearItIntentConstants;

public class NearNotificationIntentService extends NotificationsIntentService {

    private Analytics analytics;

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationComponent component = NotificationInjector.obtain(this);
        analytics = component.analytics();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String proximityId = intent.getStringExtra(NearItIntentConstants.RECIPE_ID);
        String notificationText = intent.getStringExtra(NearItIntentConstants.NOTIF_BODY);
        analytics.trackProximityIdShown(proximityId);

        List<Notification> notifications = new ArrayList<>();
        notifications.add(
                notificationCreator.createFromProximity(proximityId, notificationText)
        );
        notifier.showNotifications(notifications);
        NearNotificationReceiver.completeWakefulIntent(intent);
    }
}
