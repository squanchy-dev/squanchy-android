package net.squanchy.notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Comparator;

import net.squanchy.schedule.domain.view.Event;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import io.reactivex.Observable;

public class NotificationService extends IntentService {

    private static final Minutes NOTIFICATION_INTERVAL = Minutes.minutes(10);

    public NotificationService() {
        super(NotificationService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationCreator notificationCreator = new NotificationCreator(this);
        Notifier notifier = Notifier.from(this);

        DateTime now = new DateTime();
        DateTime notificationIntervalEnd2 = now.withPeriodAdded(NOTIFICATION_INTERVAL, 1);

        Observable<Event> favourites = Observable.empty(); // TODO load all events from somewhere
        Observable<Event> sortedFavourites = favourites.sorted(byStartDate());

        sortedFavourites
                .filter(event -> startingIn(now, event, notificationIntervalEnd2))
                .toList()
                .map(notificationCreator::createFrom)
                .subscribe(notifier::showNotifications);

        sortedFavourites
                .filter(event -> event.start().isAfter(notificationIntervalEnd2))
                .take(1)
                .subscribe(this::scheduleNextAlarm);
    }

    private boolean startingIn(DateTime now, Event event, DateTime notificationIntervalEnd2) {
        DateTime eventStart = event.start();
        return eventStart.isAfterNow() && (eventStart.isBefore(notificationIntervalEnd2) || eventStart.isEqual(notificationIntervalEnd2));
    }

    private Comparator<Event> byStartDate() {
        return (event1, event2) -> event1.start().compareTo(event2.start());
    }

    private void scheduleNextAlarm(Event event) {
        DateTime serviceAlarm = event.start().withPeriodAdded(NOTIFICATION_INTERVAL, -1);

        Intent serviceIntent = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, serviceAlarm.getMillis(), pendingIntent);
    }
}
