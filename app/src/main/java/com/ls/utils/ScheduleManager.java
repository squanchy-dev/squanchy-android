package com.ls.utils;

import com.ls.drupalconapp.model.data.EventDetailsEvent;
import com.ls.services.NotifyService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class ScheduleManager {
    private Context mContext;
    private final AlarmManager am;

    public ScheduleManager(Context context) {
        mContext = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarmForNotification(Calendar calendar, EventDetailsEvent event, long day){
        new AlarmTask(mContext, calendar, am, event, day).run();
    }

    public void cancelAlarm(long id){
        Intent intent = new Intent(mContext, NotifyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, (int) id, intent, PendingIntent.FLAG_ONE_SHOT);
        am.cancel(pendingIntent);
    }

}
