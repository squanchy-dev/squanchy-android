package com.connfa.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.connfa.model.data.EventDetailsEvent;
import com.connfa.receiver.NotifyReceiver;

public class ScheduleManager {
    private Context mContext;
    private final AlarmManager am;

    public ScheduleManager(Context context) {
        mContext = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarmForNotification(EventDetailsEvent event, long startMillis, long startDay) {
        new AlarmTask(mContext, am, event, startMillis, startDay).run();
    }

    public void cancelAlarm(long id) {
        Intent intent = new Intent(mContext, NotifyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);
    }

}
