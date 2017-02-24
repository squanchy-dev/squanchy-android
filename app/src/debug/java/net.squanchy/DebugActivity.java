package net.squanchy;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.notification.NotificationCreator;
import net.squanchy.notification.NotificationService;
import net.squanchy.notification.Notifier;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.speaker.domain.view.Speaker;

@SuppressWarnings("checkstyle:magicnumber")
public class DebugActivity extends Activity {

    private NotificationCreator notificationCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        Button buttonSingleNotification = (Button) findViewById(R.id.button_test_single_notification);
        buttonSingleNotification.setOnClickListener(view -> testSingleNotification());

        Button buttonMultipleNotifications = (Button) findViewById(R.id.button_test_multiple_notifications);
        buttonMultipleNotifications.setOnClickListener(view -> testMultipleNotifications());

        Button buttonService = (Button) findViewById(R.id.button_test_service);
        buttonService.setOnClickListener(view -> testService());

        notificationCreator = new NotificationCreator(this);
    }

    private void testSingleNotification() {
        createAndNotifyTalksCount(1);
    }

    private void testMultipleNotifications() {
        createAndNotifyTalksCount(3);
    }

    private void createAndNotifyTalksCount(int count) {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            events.add(createTestEvent(i));
        }
        List<Notification> notifications = notificationCreator.createFrom(events);

        Notifier notifier = Notifier.from(this);
        notifier.showNotifications(notifications);
    }

    private Event createTestEvent(int id) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        Date start = calendar.getTime();
        calendar.add(Calendar.MINUTE, 45);
        Date end = calendar.getTime();
        return Event.create(
                id,
                1,
                start,
                end,
                "A very interesting talk",
                "That room over there",
                ExperienceLevel.ADVANCED,
                createTalkSpeakers()
        );
    }

    private List<Speaker> createTalkSpeakers() {
        List<Speaker> speakers = new ArrayList<>(2);
        FirebaseSpeaker firebaseSpeaker = new FirebaseSpeaker();
        firebaseSpeaker.speakerId = 101L;
        firebaseSpeaker.firstName = "Ajeje";
        firebaseSpeaker.lastName = "Brazorf";
        firebaseSpeaker.jobTitle = "Uber-Experienced Pusher of PHP";
        firebaseSpeaker.avatarImageURL = "https://yt3.ggpht.com/-d35Rq8vqvmE/AAAAAAAAAAI/AAAAAAAAAAA/zy1VyiRTNec/s900-c-k-no-mo-rj-c0xffffff/photo.jpg";

        speakers.add(Speaker.create(firebaseSpeaker));
        return speakers;
    }

    private void testService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
    }
}
