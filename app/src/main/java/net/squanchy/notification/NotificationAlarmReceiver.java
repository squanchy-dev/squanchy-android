package net.squanchy.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO implement this with the new models (see SQ-55)
    }

//    private void showNotification(Context context, Event event, List<Speaker> speakerList, long dayId) {
//        String title = event.getEventName();
//        int icon = android.R.drawable.ic_dialog_info;
//
//        Intent intent = new Intent(context, EventDetailsActivity.class);
//        intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, id);
//        intent.putExtra(EventDetailsActivity.EXTRA_DAY, dayId);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setSmallIcon(icon)
//                .setContentTitle(title)
//                .setContentText(createContentText(context, event))
//                .setAutoCancel(true)
//                .setContentIntent(contentIntent)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(createBigText(context, event, speakerList)))
//                .setDefaults(Notification.DEFAULT_ALL);
//
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0, builder.build());
//    }
//
//    private CharSequence createContentText(Context context, EventDetailsEvent event) {
//        return context.getString(R.string.start_in_5_minutes_in_place, event.getPlace());
//    }
//
//    private CharSequence createBigText(Context context, EventDetailsEvent event, List<Speaker> speakerList) {
//        String speakersNames = createSpeakersNames(speakerList);
//        return context.getString(R.string.start_in_5_minutes_in_place_speakers, event.getPlace(), speakersNames);
//    }
//
//    private String createSpeakersNames(List<Speaker> speakerList) {
//        if (speakerList.isEmpty()) {
//            return "";
//        }
//        StringBuilder sb = new StringBuilder("by ");
//        for (int i = 0; i < speakerList.size(); i++) {
//            sb.append(createSpeakerFullName(speakerList.get(i)));
//            if (isBeforeSecondToLast(speakerList, i)) {
//                sb.append(", ");
//            } else if (isSecondToLast(speakerList, i)) {
//                sb.append(" and ");
//            }
//        }
//        return sb.toString();
//    }
//
//    private boolean isBeforeSecondToLast(List<Speaker> speakerList, int i) {
//        return i < speakerList.size() - 2;
//    }
//
//    private boolean isSecondToLast(List<Speaker> speakerList, int i) {
//        return i == speakerList.size() - 2;
//    }
//
//    private String createSpeakerFullName(Speaker speaker) {
//        return speaker.getFirstName() + " " + speaker.getLastName();
//    }
}
