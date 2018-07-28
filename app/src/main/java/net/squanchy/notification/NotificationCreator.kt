package net.squanchy.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import net.squanchy.R
import net.squanchy.eventdetails.EventDetailsActivity
import net.squanchy.home.HomeActivity
import net.squanchy.schedule.domain.view.Event
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.android
import net.squanchy.support.lang.getOrThrow
import net.squanchy.support.lang.or

class NotificationCreator(private val context: Context) {

    fun createFrom(events: List<Event>): List<Notification> {
        if (android.isAtLeastOreo) {
            createChannel()
        }

        val notifications = events
            .map(::createFrom)
            .toMutableList()

        if (events.size > 1) {
            notifications.add(createSummaryNotification(events))
        }
        return notifications
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            EVENTS_ABOUT_TO_START_CHANNEL_ID,
            context.getString(R.string.event_notification_starting_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = context.getString(R.string.event_notification_starting_channel_description)
        channel.enableLights(true)
        channel.lightColor = context.getColor(R.color.notification_led_color)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createFrom(event: Event): Notification {
        val notificationBuilder = createDefaultBuilder(1)
        notificationBuilder
            .setContentIntent(createPendingIntentForSingleEvent(event.id))
            .setContentTitle(event.title)
            .setColor(getTrackColor(event))
            .setWhen(event.startTime.toDateTime().millis)
            .setShowWhen(true)
            .setGroup(GROUP_KEY_NOTIFY_SESSION)

        val placeName = getPlaceName(event)
        if (!placeName.isNullOrEmpty()) {
            notificationBuilder.setContentText(placeName)
        }

        val richNotification = createBigTextRichNotification(notificationBuilder, event)

        return richNotification.build()
    }

    private fun createSummaryNotification(events: List<Event>): Notification {
        val summaryBuilder = createDefaultBuilder(events.size)
        summaryBuilder
            .setContentIntent(createPendingIntentForMultipleEvents())
            .setContentTitle(createSummaryTitle(events.size))
            .setGroup(GROUP_KEY_NOTIFY_SESSION)
            .setGroupSummary(true)
            .setLocalOnly(true)

        val richNotification = createInboxStyleRichNotification(summaryBuilder, events)

        return richNotification.build()
    }

    private fun createDefaultBuilder(talksCount: Int): NotificationCompat.Builder {
        val resources = context.resources

        val extender = NotificationCompat.WearableExtender()
        extender.background = BitmapFactory.decodeResource(resources, R.drawable.notification_background)

        return NotificationCompat.Builder(context, EVENTS_ABOUT_TO_START_CHANNEL_ID)
            .setTicker(
                context.resources.getQuantityString(
                    R.plurals.event_notification_ticker,
                    talksCount,
                    talksCount
                )
            )
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
            .setLights(
                ContextCompat.getColor(context, R.color.notification_led_color),
                NOTIFICATION_LED_ON_MS,
                NOTIFICATION_LED_OFF_MS
            )
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setAutoCancel(true)
            .extend(extender)
    }

    private fun createPendingIntentForSingleEvent(eventId: String): PendingIntent {
        val taskBuilder = createBaseTaskStackBuilder()
        val eventDetailIntent = EventDetailsActivity.createIntent(context, eventId)
        taskBuilder.addNextIntent(eventDetailIntent)

        return taskBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT)!!
    }

    private fun getPlaceName(event: Event): String? {
        return event.place.orNull()?.name
    }

    private fun getTrackColor(event: Event): Int {
        return event.track
            .map { Color.parseColor(it.accentColor ?: ARGB_TRANSPARENT) }
            .or(Color.TRANSPARENT)
    }

    private fun createPendingIntentForMultipleEvents(): PendingIntent {
        val taskBuilder = createBaseTaskStackBuilder()
        return taskBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT)!!
    }

    private fun createBaseTaskStackBuilder(): TaskStackBuilder {
        val homescreenIntent = Intent(context, HomeActivity::class.java)
        homescreenIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return TaskStackBuilder.create(context)
            .addNextIntent(homescreenIntent)
    }

    private fun createBigTextRichNotification(notificationBuilder: NotificationCompat.Builder, event: Event): NotificationCompat.BigTextStyle {
        val bigTextBuilder = StringBuilder()
            .append(getSpeakerNamesFrom(event.speakers))

        val placeName = getPlaceName(event)
        if (!placeName.isNullOrEmpty()) {
            bigTextBuilder
                .append('\n')
                .append(context.getString(R.string.event_notification_starting_in, placeName))
        }

        return NotificationCompat.BigTextStyle(notificationBuilder)
            .setBigContentTitle(event.title)
            .bigText(bigTextBuilder.toString())
    }

    private fun getSpeakerNamesFrom(speakers: List<Speaker>): String {
        val speakerNames = speakers.joinToString(separator = ", ", transform = { it.name })

        return context.getString(R.string.event_notification_starting_by, speakerNames)
    }

    private fun createInboxStyleRichNotification(
        notificationBuilder: NotificationCompat.Builder,
        events: List<Event>
    ): NotificationCompat.InboxStyle {
        val bigContentTitle = createSummaryTitle(events.size)
        val richNotification = NotificationCompat.InboxStyle(notificationBuilder)
            .setBigContentTitle(bigContentTitle)

        for (event in events) {
            if (event.place.isDefined()) {
                richNotification.addLine(
                    context.getString(
                        R.string.room_event_notification,
                        event.place.getOrThrow().name,
                        event.title
                    )
                )
            } else {
                richNotification.addLine(event.title)
            }
        }

        return richNotification
    }

    private fun createSummaryTitle(talksCount: Int): String {
        val quantityString = context.resources.getQuantityString(R.plurals.event_notification_count_starting, talksCount)
        return String.format(quantityString, talksCount)
    }

    companion object {
        private const val GROUP_KEY_NOTIFY_SESSION = "group_key_notify_session"
        private const val EVENTS_ABOUT_TO_START_CHANNEL_ID = "events_about_to_start"

        // pulsate every 1 second, indicating a relatively high degree of urgency
        private const val NOTIFICATION_LED_ON_MS = 100
        private const val NOTIFICATION_LED_OFF_MS = 1000
        private const val ARGB_TRANSPARENT = "#00000000"
    }
}
