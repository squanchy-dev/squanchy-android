package net.squanchy.notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Event;

import org.joda.time.LocalDateTime;

import io.reactivex.Observable;
import timber.log.Timber;

import static net.squanchy.support.lang.Lists.filter;

public class NotificationsIntentService extends IntentService {

    private static final int NOTIFICATION_INTERVAL_MINUTES = 10;
    private static final boolean SHOW_NOTIFICATIONS_DEFAULT = true;

    private NotificationService service;
    protected NotificationCreator notificationCreator;
    protected Notifier notifier;
    protected SharedPreferences preferences;

    public NotificationsIntentService() {
        super(NotificationsIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationComponent component = NotificationInjector.obtain(this);
        service = component.service();
        notificationCreator = component.notificationCreator();
        notifier = component.notifier();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LocalDateTime notificationIntervalEnd = new LocalDateTime().plusMinutes(NOTIFICATION_INTERVAL_MINUTES);

        Observable<List<Event>> sortedFavourites = service.sortedFavourites();

        LocalDateTime now = new LocalDateTime();
        if (shouldShowNotifications()) {
            sortedFavourites
                    .map(events -> filter(events, event -> event.getStartTime().isAfter(now)))
                    .map(events -> filter(events, event -> isBeforeOrEqualTo(event.getStartTime(), notificationIntervalEnd)))
                    .map(notificationCreator::createFrom)
                    .subscribe(notifier::showNotifications);
        }

        sortedFavourites
                .map(events -> filter(events, event -> event.getStartTime().isAfter(notificationIntervalEnd)))
                .subscribe(this::scheduleNextAlarm);
    }

    private boolean shouldShowNotifications() {
        String notificationPreferenceKey = getString(R.string.about_to_start_notification_preference_key);
        return preferences.getBoolean(notificationPreferenceKey, SHOW_NOTIFICATIONS_DEFAULT);
    }

    private boolean isBeforeOrEqualTo(LocalDateTime start, LocalDateTime notificationIntervalEnd) {
        return start.isBefore(notificationIntervalEnd) || start.isEqual(notificationIntervalEnd);
    }

    private void scheduleNextAlarm(List<Event> events) {
        if (events.isEmpty()) {
            Timber.d("no events");
            return;
        }
        Event firstEvent = events.get(0);
        LocalDateTime serviceAlarm = firstEvent.getStartTime().minusMinutes(NOTIFICATION_INTERVAL_MINUTES);
        Timber.d("Next alarm scheduled for " + serviceAlarm.toString());

        Intent serviceIntent = new Intent(this, NotificationsIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, serviceAlarm.toDateTime().getMillis(), pendingIntent);
    }
}
