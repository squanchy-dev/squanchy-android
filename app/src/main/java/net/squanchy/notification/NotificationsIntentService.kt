package net.squanchy.notification

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import io.reactivex.disposables.CompositeDisposable
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import org.joda.time.LocalDateTime
import timber.log.Timber

class NotificationsIntentService : IntentService(NotificationsIntentService::class.java.simpleName) {

    private lateinit var service: NotificationService
    private lateinit var notificationCreator: NotificationCreator
    private lateinit var notifier: Notifier
    private lateinit var preferences: SharedPreferences

    private lateinit var subscriptions: CompositeDisposable

    override fun onCreate() {
        super.onCreate()

        subscriptions = CompositeDisposable()

        val component = notificationComponent(this)
        service = component.service()
        notificationCreator = component.notificationCreator()
        notifier = component.notifier()
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        val notificationIntervalEnd = LocalDateTime().plusMinutes(NOTIFICATION_INTERVAL_MINUTES)

        val sortedFavourites = service.sortedFavourites()

        val now = LocalDateTime()
        if (shouldShowNotifications()) {
            subscriptions.add(
                    sortedFavourites
                            .map { events -> events.filter { it.startTime.isAfter(now) } }
                            .map { events -> events.filter { isBeforeOrEqualTo(it.startTime, notificationIntervalEnd) } }
                            .map { notificationCreator.createFrom(it) }
                            .subscribe(notifier::showNotifications)
            )
        }

        subscriptions.add(
                sortedFavourites
                        .map { events -> events.filter { it.startTime.isAfter(notificationIntervalEnd) } }
                        .subscribe(::scheduleNextAlarm)
        )
    }

    private fun shouldShowNotifications(): Boolean {
        val notificationPreferenceKey = getString(R.string.about_to_start_notification_preference_key)
        return preferences.getBoolean(notificationPreferenceKey, SHOW_NOTIFICATIONS_DEFAULT)
    }

    private fun isBeforeOrEqualTo(start: LocalDateTime, notificationIntervalEnd: LocalDateTime): Boolean {
        return start.isBefore(notificationIntervalEnd) || start.isEqual(notificationIntervalEnd)
    }

    private fun scheduleNextAlarm(events: List<Event>) {
        if (events.isEmpty()) {
            Timber.d("no events")
            return
        }
        val startTime = events[0].startTime
        val serviceAlarm = startTime.minusMinutes(NOTIFICATION_INTERVAL_MINUTES)
        Timber.d("Next alarm scheduled for %s", serviceAlarm.toString())

        val serviceIntent = Intent(this, NotificationsIntentService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, serviceAlarm.toDateTime().millis, pendingIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }

    companion object {
        private val NOTIFICATION_INTERVAL_MINUTES = 10
        private val SHOW_NOTIFICATIONS_DEFAULT = true
    }
}
