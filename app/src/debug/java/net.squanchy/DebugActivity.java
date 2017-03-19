package net.squanchy;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.notification.NotificationCreator;
import net.squanchy.notification.NotificationsIntentService;
import net.squanchy.notification.Notifier;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Place;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Optional;

import org.joda.time.LocalDateTime;

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
        LocalDateTime start = new LocalDateTime().plusMinutes(5);
        LocalDateTime end = new LocalDateTime().plusMinutes(45);
        return Event.create(
                String.valueOf(id),
                id,
                "1",
                start,
                end,
                "A very interesting talk",
                createPlace(),
                Optional.of(ExperienceLevel.ADVANCED),
                createTalkSpeakers(),
                Event.Type.TALK,
                true,
                Optional.absent(),
                Optional.absent()
        );
    }

    private Optional<Place> createPlace() {
        Place place = Place.create("1", "That room over there", Optional.absent());
        return Optional.of(place);
    }

    private List<Speaker> createTalkSpeakers() {
        List<Speaker> speakers = new ArrayList<>(2);
        speakers.add(Speaker.create(
                "1",
                101L,
                "Ajeje Brazorf",
                "https://yt3.ggpht.com/-d35Rq8vqvmE/AAAAAAAAAAI/AAAAAAAAAAA/zy1VyiRTNec/s900-c-k-no-mo-rj-c0xffffff/photo.jpg"
                )
        );
        return speakers;
    }

    private void testService() {
        Intent serviceIntent = new Intent(this, NotificationsIntentService.class);
        startService(serviceIntent);
    }
}
