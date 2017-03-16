package net.squanchy.notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Comparator;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Event;

import org.joda.time.LocalDateTime;

import io.reactivex.Observable;

public class NotificationService extends IntentService {

    private static final int NOTIFICATION_INTERVAL_MINUTES = 10;
    private static final boolean SHOW_NOTIFICATIONS_DEFAULT = true;

    private NotificationCreator notificationCreator;
    private Notifier notifier;
    private SharedPreferences preferences;

    public NotificationService() {
        super(NotificationService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationCreator = new NotificationCreator(this);
        notifier = Notifier.from(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LocalDateTime notificationIntervalEnd = new LocalDateTime().plusMinutes(NOTIFICATION_INTERVAL_MINUTES);

        Observable<Event> favourites = Observable.empty(); // TODO load all events from somewhere
        Observable<Event> sortedFavourites = favourites.sorted(byStartDate());

        LocalDateTime now = new LocalDateTime();
        if (shouldShowNotifications()) {
            sortedFavourites
                    .filter(event -> event.startTime().isAfter(now))
                    .filter(event -> isBeforeOrEqualTo(event.startTime(), notificationIntervalEnd))
                    .toList()
                    .map(notificationCreator::createFrom)
                    .subscribe(notifier::showNotifications);
        }

        sortedFavourites
                .filter(event -> event.startTime().isAfter(notificationIntervalEnd))
                .take(1)
                .subscribe(this::scheduleNextAlarm);
    }

    private boolean shouldShowNotifications() {
        String notificationPreferenceKey = getString(R.string.about_to_start_notification_preference_key);
        return preferences.getBoolean(notificationPreferenceKey, SHOW_NOTIFICATIONS_DEFAULT);
    }

    private boolean isBeforeOrEqualTo(LocalDateTime start, LocalDateTime notificationIntervalEnd) {
        return start.isBefore(notificationIntervalEnd) || start.isEqual(notificationIntervalEnd);
    }

    private Comparator<Event> byStartDate() {
        return (event1, event2) -> event1.startTime().compareTo(event2.startTime());
    }

    private void scheduleNextAlarm(Event event) {
        LocalDateTime serviceAlarm = event.startTime().minusMinutes(NOTIFICATION_INTERVAL_MINUTES);

        Intent serviceIntent = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, serviceAlarm.toDateTime().getMillis(), pendingIntent);
    }
}
