package net.squanchy.home;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.support.system.CurrentTime;

import io.reactivex.Maybe;
import io.reactivex.Observable;

public class CurrentEventService {

    private final EventRepository eventRepository;
    private final FirebaseAuthService authService;
    private final CurrentTime currentTime;

    public CurrentEventService(EventRepository eventRepository, FirebaseAuthService authService, CurrentTime currentTime) {
        this.eventRepository = eventRepository;
        this.authService = authService;
        this.currentTime = currentTime;
    }

    public Maybe<Event> eventIn(String placeId) {
        return authService.ifUserSignedInThenObservableFrom(eventRepository::events)
                .flatMap(Observable::fromIterable)
                .filter(event -> event.getPlace().isPresent())
                .filter(event -> event.getPlace().get().getId().equals(placeId))
                .filter(event -> event.isHappeningAt(currentTime.currentLocalDateTime()))
                .firstElement();
    }
}
