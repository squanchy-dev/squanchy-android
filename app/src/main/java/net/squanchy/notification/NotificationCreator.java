package net.squanchy.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.R;
import net.squanchy.eventdetails.EventDetailsActivity;
import net.squanchy.home.HomeActivity;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Place;
import net.squanchy.speaker.domain.view.Speaker;

import static net.squanchy.support.lang.Lists.map;

public class NotificationCreator {

    private static final String GROUP_KEY_NOTIFY_SESSION = "group_key_notify_session";

    // pulsate every 1 second, indicating a relatively high degree of urgency
    private static final int NOTIFICATION_LED_ON_MS = 100;
    private static final int NOTIFICATION_LED_OFF_MS = 1000;
    private static final String EMPTY_PLACE_NAME = "";
    private static final String ARGB_TRANSPARENT = "#00000000";

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
                .setContentIntent(createPendingIntentForSingleEvent(event.getId()))
                .setContentTitle(event.getTitle())
                .setColor(getTrackColor(event))
                .setWhen(event.getStartTime().toDateTime().getMillis())
                .setShowWhen(true)
                .setGroup(GROUP_KEY_NOTIFY_SESSION);

        String placeName = getPlaceName(event);
        if (!placeName.isEmpty()) {
            notificationBuilder.setContentText(placeName);
        }

        NotificationCompat.BigTextStyle richNotification = createBigTextRichNotification(notificationBuilder, event);

        return richNotification.build();
    }

    public Notification createFromProximity(String proximityId, String bigText, String smallText) {
        Resources resources = context.getResources();

        NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender();
        extender.setBackground(BitmapFactory.decodeResource(resources, R.drawable.notification_background));

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setContentIntent(createPendingIntentForProximityNotification(proximityId))
                        .setContentTitle(getApplicationName(context))
                        .setContentText(smallText)
                        .setSmallIcon(R.drawable.ic_place_white_24dp)
                        .setLights(
                                ContextCompat.getColor(context, R.color.notification_led_color),
                                NOTIFICATION_LED_ON_MS,
                                NOTIFICATION_LED_OFF_MS
                        )
                        .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(bigText))
                        .setPriority(Notification.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .extend(extender);
        return builder.build();
    }

    private String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
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

    private PendingIntent createPendingIntentForProximityNotification(String proximityId) {
        TaskStackBuilder taskBuilder = createBaseTaskStackBuilder();
        Intent homeIntent = HomeActivity.createProximityIntent(context, proximityId);
        taskBuilder.addNextIntent(homeIntent);

        return taskBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private NotificationCompat.Builder createDefaultBuilder(int talksCount) {
        Resources resources = context.getResources();

        NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender();
        extender.setBackground(BitmapFactory.decodeResource(resources, R.drawable.notification_background));

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
                        ContextCompat.getColor(context, R.color.notification_led_color),
                        NOTIFICATION_LED_ON_MS,
                        NOTIFICATION_LED_OFF_MS
                )
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .extend(extender);
    }

    private PendingIntent createPendingIntentForSingleEvent(String eventId) {
        TaskStackBuilder taskBuilder = createBaseTaskStackBuilder();
        Intent eventDetailIntent = EventDetailsActivity.createIntent(context, eventId);
        taskBuilder.addNextIntent(eventDetailIntent);

        return taskBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private String getPlaceName(Event event) {
        return event.getPlace().map(Place::getName).or(EMPTY_PLACE_NAME);
    }

    private int getTrackColor(Event event) {
        return event.getTrack()
                .map(track -> Color.parseColor(track.getAccentColor().or(ARGB_TRANSPARENT)))
                .or(Color.TRANSPARENT);
    }

    private PendingIntent createPendingIntentForMultipleEvents() {
        TaskStackBuilder taskBuilder = createBaseTaskStackBuilder();
        return taskBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private TaskStackBuilder createBaseTaskStackBuilder() {
        Intent homescreenIntent = new Intent(context, HomeActivity.class);
        homescreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return TaskStackBuilder.create(context)
                .addNextIntent(homescreenIntent);
    }

    private NotificationCompat.BigTextStyle createBigTextRichNotification(NotificationCompat.Builder notificationBuilder, Event event) {
        StringBuilder bigTextBuilder = new StringBuilder()
                .append(getSpeakerNamesFrom(event.getSpeakers()));

        String placeName = getPlaceName(event);
        if (!placeName.isEmpty()) {
            bigTextBuilder
                    .append('\n')
                    .append(context.getString(R.string.event_notification_starting_in, placeName));
        }

        return new NotificationCompat.BigTextStyle(notificationBuilder)
                .setBigContentTitle(event.getTitle())
                .bigText(bigTextBuilder.toString());
    }

    private String getSpeakerNamesFrom(List<Speaker> speakers) {
        String speakerNames = TextUtils.join(", ", map(speakers, Speaker::getName));
        return context.getString(R.string.event_notification_starting_by, speakerNames);
    }

    private NotificationCompat.InboxStyle createInboxStyleRichNotification(NotificationCompat.Builder notificationBuilder, List<Event> events) {
        String bigContentTitle = createSummaryTitle(events.size());
        NotificationCompat.InboxStyle richNotification = new NotificationCompat.InboxStyle(notificationBuilder)
                .setBigContentTitle(bigContentTitle);

        for (Event event : events) {
            if (event.getPlace().isPresent()) {
                richNotification.addLine(
                        context.getString(
                                R.string.room_event_notification,
                                event.getPlace().get().getName(),
                                event.getTitle()
                        )
                );
            } else {
                richNotification.addLine(event.getTitle());
            }
        }

        return richNotification;
    }

    private String createSummaryTitle(int talksCount) {
        return context.getString(R.string.event_notification_count_starting, talksCount);
    }
}
