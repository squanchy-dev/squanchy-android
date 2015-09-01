package com.ls.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.data.EventDetailsEvent;
import com.ls.receiver.NotifyReceiver;

import java.util.Calendar;

public class AlarmTask implements Runnable{
    private final Calendar date;
    private final AlarmManager am;
    private final Context context;
    private final EventDetailsEvent event;
    private final long day;

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_DAY = "EXTRA_DAY";
    public static final String EXTRA_TEXT = "EXTRA_TEXT";

    public AlarmTask(Context context, Calendar date, AlarmManager am, EventDetailsEvent event, long day) {
        this.context = context;
        this.am = am;
        this.date = date;
        this.event = event;
        this.day = day;
    }

    @Override
    public void run() {
        Intent intent = new Intent(context, NotifyReceiver.class);
        intent.putExtra(EXTRA_ID, event.getEventId());
        intent.putExtra(EXTRA_DAY, day);
        String notifyText = event.getEventName() + context.getString(R.string.start_in_5_minutes);
        intent.putExtra(EXTRA_TEXT, notifyText);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) event.getEventId(), intent, PendingIntent.FLAG_ONE_SHOT);

        long fiveMinutes = 5*60*1000;

        am.set(AlarmManager.RTC, date.getTimeInMillis() - fiveMinutes, pendingIntent);
    }
}
