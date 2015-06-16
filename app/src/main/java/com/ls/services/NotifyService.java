package com.ls.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.ui.activity.EventDetailsActivity;
import com.ls.drupalconapp.ui.activity.HomeActivity;
import com.ls.utils.AlarmTask;

public class NotifyService extends IntentService{

    public NotifyService() {
        super("NotifyService");
    }

    private NotificationManager mNM;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("NotifyService", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long eventId = intent.getLongExtra(AlarmTask.EXTRA_ID, -1);
        long day = intent.getLongExtra(AlarmTask.EXTRA_DAY, -1);
        String text = intent.getStringExtra(AlarmTask.EXTRA_TEXT);
        showNotification(eventId, day, text);
    }

    private void showNotification(long id, long day ,String text) {
        Log.d("show", "showNotification");
        String title = this.getString(R.string.dont_miss_it);
        int icon = android.R.drawable.ic_dialog_info;
        long time = System.currentTimeMillis();

        Notification notification = new Notification(icon, text, time);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, id);
        intent.putExtra(EventDetailsActivity.EXTRA_DAY, day);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        notification.setLatestEventInfo(this, title, text, contentIntent);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

        // Send the notification to the system.
        mNM.notify((int) id, notification);

        stopSelf();
    }
}
