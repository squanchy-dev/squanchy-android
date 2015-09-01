package com.ls.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ls.drupalconapp.R;
import com.ls.ui.activity.EventDetailsActivity;
import com.ls.ui.activity.HomeActivity;
import com.ls.utils.AlarmTask;

public class NotifyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long eventId = intent.getLongExtra(AlarmTask.EXTRA_ID, -1);
        long day = intent.getLongExtra(AlarmTask.EXTRA_DAY, -1);
        String text = intent.getStringExtra(AlarmTask.EXTRA_TEXT);
        showNotification(context, eventId, day, text);
    }

    private void showNotification(Context context, long id, long day ,String text) {
        String title = context.getString(R.string.dont_miss_it);
        int icon = android.R.drawable.ic_dialog_info;
        long time = System.currentTimeMillis();

        Notification notification = new Notification(icon, text, time);

        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, id);
        intent.putExtra(EventDetailsActivity.EXTRA_DAY, day);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, title, text, contentIntent);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
