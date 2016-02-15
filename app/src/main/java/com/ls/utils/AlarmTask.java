package com.ls.utils;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.data.EventDetailsEvent;
import com.ls.receiver.NotifyReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmTask implements Runnable {

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_DAY = "EXTRA_DAY";
    public static final String EXTRA_TEXT = "EXTRA_TEXT";
    private static final int FIVE_MINUTES = 5 * 60 * 1000;

    private final AlarmManager am;
    private final Context context;
    private final EventDetailsEvent event;
    private final long startMillis;
    private final long day;

    public AlarmTask(Context context, AlarmManager am, EventDetailsEvent event, long startMillis, long day) {
        this.context = context;
        this.am = am;
        this.event = event;
        this.startMillis = startMillis;
        this.day = day;
    }

    @Override
    public void run() {
        Intent intent = new Intent(context, NotifyReceiver.class);
        intent.putExtra(EXTRA_ID, event.getEventId());
        intent.putExtra(EXTRA_DAY, day);

        String notifyText = event.getEventName() + context.getString(R.string.start_in_5_minutes);
        intent.putExtra(EXTRA_TEXT, notifyText);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) event.getEventId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        am.set(AlarmManager.RTC_WAKEUP, startMillis - FIVE_MINUTES, pendingIntent);
    }
}
