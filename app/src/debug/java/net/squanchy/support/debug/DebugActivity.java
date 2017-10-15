package net.squanchy.support.debug;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.squanchy.R;
import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.notification.NotificationCreator;
import net.squanchy.notification.NotificationsIntentService;
import net.squanchy.notification.Notifier;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Place;
import net.squanchy.schedule.domain.view.Track;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Optional;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

@SuppressWarnings("MagicNumber")    // It's because there's a bunch of test data
public class DebugActivity extends TypefaceStyleableActivity {

    private NotificationCreator notificationCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        Button buttonSingleNotification = findViewById(R.id.button_test_single_notification);
        buttonSingleNotification.setOnClickListener(view -> testSingleNotification());

        Button buttonMultipleNotifications = findViewById(R.id.button_test_multiple_notifications);
        buttonMultipleNotifications.setOnClickListener(view -> testMultipleNotifications());

        Button buttonService = findViewById(R.id.button_test_service);
        buttonService.setOnClickListener(view -> testService());

        Button buttonResetOnboarding = findViewById(R.id.button_reset_onboarding);
        buttonResetOnboarding.setOnClickListener(view -> resetOnboarding());

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

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        Notifier notifier = new Notifier(notificationManagerCompat);
        notifier.showNotifications(notifications);
    }

    private Event createTestEvent(int id) {
        LocalDateTime start = new LocalDateTime().plusMinutes(5);
        LocalDateTime end = new LocalDateTime().plusMinutes(45);
        return Event.Companion.create(
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
                Optional.of(createTrack()),
                DateTimeZone.forID("Europe/Rome")
        );
    }

    private Optional<Place> createPlace() {
        Place place = Place.Companion.create("1", "That room over there", Optional.absent());
        return Optional.of(place);
    }

    private List<Speaker> createTalkSpeakers() {
        List<Speaker> speakers = new ArrayList<>(2);
        speakers.add(Speaker.Companion.create(
                "1",
                101L,
                "Ajeje Brazorf",
                "An Android dev",
                Optional.absent(),
                Optional.absent(),
                Optional.absent(),
                Optional.of("https://yt3.ggpht.com/-d35Rq8vqvmE/AAAAAAAAAAI/AAAAAAAAAAA/zy1VyiRTNec/s900-c-k-no-mo-rj-c0xffffff/photo.jpg"),
                Optional.absent()
                )
        );
        return speakers;
    }

    private Track createTrack() {
        return Track.Companion.create(
                "0",
                "UI",
                Optional.of(generateColor()),
                Optional.of(generateColor()),
                Optional.of("gs://droidcon-italy-2017.appspot.com/tracks/0.webp")
        );
    }

    private String generateColor() {
        Random r = new Random();
        final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] s = new char[7];
        int n = r.nextInt(0x1000000);

        s[0] = '#';
        for (int i = 1; i < 7; i++) {
            s[i] = hex[n & 0xf];
            n >>= 4;
        }
        return new String(s);
    }

    private void testService() {
        Intent serviceIntent = new Intent(this, NotificationsIntentService.class);
        startService(serviceIntent);
    }

    private void resetOnboarding() {
        new OnboardingResetter(this).resetOnboarding();
        Snackbar.make(findViewById(R.id.debug_root), "It's daaaawnnnn", Snackbar.LENGTH_SHORT).show();
    }
}
