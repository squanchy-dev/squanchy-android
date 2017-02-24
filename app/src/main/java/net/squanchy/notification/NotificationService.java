package net.squanchy.notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import net.squanchy.schedule.domain.view.Event;

import io.reactivex.Observable;

public class NotificationService extends IntentService {

    private static final int NOTIFICATION_INTERVAL_MINUTES = 10;

    public NotificationService() {
        super(NotificationService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationCreator notificationCreator = new NotificationCreator(this);
        Notifier notifier = Notifier.from(this);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, NOTIFICATION_INTERVAL_MINUTES);
        Date notificationIntervalEnd = calendar.getTime();
        Date now = new Date();

        Observable<Event> favourites = Observable.empty(); // TODO load all events from somewhere
        Observable<Event> sortedFavourites = favourites.sorted(byStartDate());

        sortedFavourites
                .filter(event -> startingIn(now, notificationIntervalEnd, event))
                .toList()
                .map(notificationCreator::createFrom)
                .subscribe(notifier::showNotifications);

        sortedFavourites
                .filter(event -> event.start().after(notificationIntervalEnd))
                .take(1)
                .subscribe(this::scheduleNextAlarm);
    }

    private boolean startingIn(Date now, Date notificationIntervalEnd, Event event) {
        Date eventStart = event.start();
        return now.before(eventStart) && (eventStart.before(notificationIntervalEnd) || eventStart == notificationIntervalEnd);
    }

    private Comparator<Event> byStartDate() {
        return (event1, event2) -> event1.start().compareTo(event2.start());
    }

    private void scheduleNextAlarm(Event event) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.start());
        calendar.add(Calendar.MINUTE, -NOTIFICATION_INTERVAL_MINUTES);

        Intent serviceIntent = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
