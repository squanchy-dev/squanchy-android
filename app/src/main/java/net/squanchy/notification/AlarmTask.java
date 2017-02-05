package net.squanchy.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

public class AlarmTask implements Runnable {

    static final String EXTRA_ID = "EXTRA_ID";
    static final String EXTRA_DAY = "EXTRA_DAY";

    private static final long FIVE_MINUTES_IN_MILLIS = TimeUnit.MINUTES.toMillis(5);

    private final AlarmManager alarmManager;
    private final Context context;
    private final long eventId;
    private final long startMillis;
    private final long day;

    public AlarmTask(Context context, AlarmManager alarmManager, long eventId, long startMillis, long day) {
        this.context = context;
        this.alarmManager = alarmManager;
        this.eventId = eventId;
        this.startMillis = startMillis;
        this.day = day;
    }

    @Override
    public void run() {
        Intent intent = new Intent(context, NotificationAlarmReceiver.class);
        intent.putExtra(EXTRA_ID, eventId);
        intent.putExtra(EXTRA_DAY, day);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) eventId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, startMillis - FIVE_MINUTES_IN_MILLIS, pendingIntent);
    }
}
