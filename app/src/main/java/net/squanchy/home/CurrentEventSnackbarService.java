package net.squanchy.home;

import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.Locale;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.support.system.CurrentTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.reactivex.Maybe;
import io.reactivex.Observable;

public class CurrentEventSnackbarService {

    private static final String WHEN_DATE_TIME_FORMAT = "HH:mm";
    private final EventRepository eventRepository;
    private final FirebaseAuthService authService;
    private final CurrentTime currentTime;

    public CurrentEventSnackbarService(EventRepository eventRepository, FirebaseAuthService authService, CurrentTime currentTime) {
        this.eventRepository = eventRepository;
        this.authService = authService;
        this.currentTime = currentTime;
    }

    public Maybe<Event> eventIn(String placeId) {
        return authService.ifUserSignedInThenObservableFrom(eventRepository::events)
                .flatMap(Observable::fromIterable)
                .filter(event -> event.place().isPresent())
                .filter(event -> event.place().get().id().equals(placeId))
                .filter(event -> event.isDuring(currentTime.getLocalDateTime()))
                .firstElement();
    }

    public Snackbar buildSnackbar(View view, Event event, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, buildString(event), BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.event_details, listener);
        snackbar.setActionTextColor(view.getResources().getColor(R.color.text_inverse));
        return snackbar;
    }

    private String buildString(Event event) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(WHEN_DATE_TIME_FORMAT)
                .withZone(event.timeZone());
        return String.format(
                Locale.US,
                "Room: %s %s %s",
                event.place().get().name(),
                event.speakersNames(),
                formatter.print(event.startTime().toDateTime())
        );
    }
}
