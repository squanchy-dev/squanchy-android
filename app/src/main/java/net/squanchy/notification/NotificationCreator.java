package net.squanchy.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.R;
import net.squanchy.eventdetails.EventDetailsActivity;
import net.squanchy.navigation.HomeActivity;
import net.squanchy.schedule.domain.view.Event;

public class NotificationCreator {

    private static final String GROUP_KEY_NOTIFY_SESSION = "group_key_notify_session";

    // pulsate every 1 second, indicating a relatively high degree of urgency
    private static final int NOTIFICATION_LED_ON_MS = 100;
    private static final int NOTIFICATION_LED_OFF_MS = 1000;

    private final Context context;

    public NotificationCreator(Context context) {
        this.context = context;
    }

    public List<Notification> createFrom(List<Event> events) {
        List<Notification> notifications = new ArrayList<>();
        for (Event event : events) {
            notifications.add(createFrom(event));
        }
        if (events.size() > 1) {
            notifications.add(createSummaryNotification(events));
        }
        return notifications;
    }

    private Notification createFrom(Event event) {
        NotificationCompat.Builder notificationBuilder = createDefaultBuilder(1);
        notificationBuilder
                .setContentIntent(createPendingIntentForSingleEvent(event.day(), event.id()))
                .setContentTitle(event.title())
                .setContentText(event.place())
                //.setColor(track.color().getIntValue()) TODO set color depending on the track
                .setUsesChronometer(true)
                .setWhen(event.start().getTime())
                .setShowWhen(true)
                .setGroup(GROUP_KEY_NOTIFY_SESSION);

        NotificationCompat.BigTextStyle richNotification = createBigTextRichNotification(notificationBuilder, event);

        return richNotification.build();
    }

    private Notification createSummaryNotification(List<Event> events) {
        NotificationCompat.Builder summaryBuilder = createDefaultBuilder(events.size());
        summaryBuilder
                .setContentIntent(createPendingIntentForMultipleEvents())
                .setContentTitle(createSummaryTitle(events.size()))
                .setGroup(GROUP_KEY_NOTIFY_SESSION)
                .setGroupSummary(true)
                .setLocalOnly(true);

        NotificationCompat.InboxStyle richNotification = createInboxStyleRichNotification(summaryBuilder, events);

        return richNotification.build();
    }

    private NotificationCompat.Builder createDefaultBuilder(int talksCount) {
        Resources resources = context.getResources();

        NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender();
        extender.setBackground(BitmapFactory.decodeResource(resources, R.drawable.notification_background));
        // TODO: update notification background

        return new NotificationCompat.Builder(context)
                .setTicker(
                        context.getResources().getQuantityString(
                                R.plurals.event_notification_ticker,
                                talksCount,
                                talksCount
                        )
                )
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setLights(
                        resources.getColor(R.color.notification_led_color, context.getTheme()),
                        NOTIFICATION_LED_ON_MS,
                        NOTIFICATION_LED_OFF_MS
                )
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .extend(extender);
    }

    private PendingIntent createPendingIntentForSingleEvent(int day, long eventId) {
        TaskStackBuilder taskBuilder = createBaseTaskStackBuilder();
        Intent eventDetailIntent = EventDetailsActivity.createIntent(context, day, eventId);
        taskBuilder.addNextIntent(eventDetailIntent);

        return taskBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private PendingIntent createPendingIntentForMultipleEvents() {
        TaskStackBuilder taskBuilder = createBaseTaskStackBuilder();
        return taskBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private TaskStackBuilder createBaseTaskStackBuilder() {
        Intent baseIntent = new Intent(context, HomeActivity.class);
        baseIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return TaskStackBuilder.create(context)
                .addNextIntent(baseIntent);
    }

    private NotificationCompat.BigTextStyle createBigTextRichNotification(NotificationCompat.Builder notificationBuilder, Event event) {
        StringBuilder bigTextBuilder = new StringBuilder()
                .append(getDisplayedSpeakers(event));
        if (event.place() != null) {
            bigTextBuilder.append(context.getString(R.string.event_notification_starting_in, event.place()));
        }
        return new NotificationCompat.BigTextStyle(notificationBuilder)
                .setBigContentTitle(event.title())
                .bigText(bigTextBuilder.toString());
    }

    private String getDisplayedSpeakers(Event event) {
        return context.getString(R.string.event_notification_starting_by, event.speakersNames()) + "\n";
    }

    private NotificationCompat.InboxStyle createInboxStyleRichNotification(NotificationCompat.Builder notificationBuilder, List<Event> events) {
        String bigContentTitle = createSummaryTitle(events.size());
        NotificationCompat.InboxStyle richNotification = new NotificationCompat.InboxStyle(notificationBuilder)
                .setBigContentTitle(bigContentTitle);
        for (Event event : events) {
            if (event.place() != null) {
                richNotification.addLine(
                        context.getString(
                                R.string.room_event_notification,
                                event.place(),
                                event.title()
                        )
                );
            } else {
                richNotification.addLine(event.title());
            }
        }

        return richNotification;
    }

    private String createSummaryTitle(int talksCount) {
        return context.getString(R.string.event_notification_count_starting, talksCount);
    }
}
