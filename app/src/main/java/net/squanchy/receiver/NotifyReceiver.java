package net.squanchy.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import net.squanchy.R;
import net.squanchy.model.Model;
import net.squanchy.model.data.EventDetailsEvent;
import net.squanchy.model.data.Speaker;
import net.squanchy.model.managers.EventManager;
import net.squanchy.model.managers.SpeakerManager;
import net.squanchy.ui.activity.EventDetailsActivity;
import net.squanchy.ui.activity.HomeActivity;
import net.squanchy.utils.AlarmTask;

import java.util.List;

import static android.R.attr.id;

public class NotifyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
         long eventId = intent.getLongExtra(AlarmTask.EXTRA_ID, -1);
         long day = intent.getLongExtra(AlarmTask.EXTRA_DAY, -1);

        SpeakerManager speakerManager = Model.getInstance().getSpeakerManager();
        List<Speaker> speakerList = speakerManager.getSpeakersByEventId(eventId);

        EventManager eventManager = Model.getInstance().getEventManager();
        EventDetailsEvent event = eventManager.getEventById(eventId);
        if (event != null) {
            showNotification(context, event, speakerList, day);
        }

    }

    private void showNotification(Context context, EventDetailsEvent event, List<Speaker> speakerList, long day) {
        String title = event.getEventName();
        int icon = android.R.drawable.ic_dialog_info;

        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, id);
        intent.putExtra(EventDetailsActivity.EXTRA_DAY, day);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(createContentText(context, event))
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                                  .bigText(createBigText(context, event, speakerList)))
                .setDefaults(Notification.DEFAULT_ALL);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private CharSequence createContentText(Context context, EventDetailsEvent event) {
        return context.getString(R.string.start_in_5_minutes_in_place, event.getPlace());
    }

    private CharSequence createBigText(Context context, EventDetailsEvent event, List<Speaker> speakerList) {
        String speakersNames = createSpeakersNames(speakerList);
        return context.getString(R.string.start_in_5_minutes_in_place_speakers,  event.getPlace(), speakersNames);
    }

    private String createSpeakersNames(List<Speaker> speakerList) {
        if (speakerList.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("by ");
        for (int i = 0; i < speakerList.size(); i++) {
            sb.append(createSpeakerFullName(speakerList.get(i)));
            if (isBeforeSecondToLast(speakerList, i)) {
                sb.append(", ");
            } else if (isSecondToLast(speakerList, i)) {
                sb.append(" and ");
            }
        }
        return sb.toString();
    }

    private boolean isBeforeSecondToLast(List<Speaker> speakerList, int i) {
        return i < speakerList.size() - 2;
    }

    private boolean isSecondToLast(List<Speaker> speakerList, int i) {
        return i == speakerList.size() - 2;
    }

    private String createSpeakerFullName(Speaker speaker) {
        return speaker.getFirstName() + " " + speaker.getLastName();
    }
}
