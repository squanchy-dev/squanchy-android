package net.squanchy.notification;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.EventRepository;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static net.squanchy.support.lang.Lists.filter;

class NotificationService {

    private final FirebaseAuthService authService;
    private final EventRepository eventRepository;

    NotificationService(FirebaseAuthService authService, EventRepository eventRepository) {
        this.authService = authService;
        this.eventRepository = eventRepository;
    }

    Observable<List<Event>> sortedFavourites() {
        return authService.ifUserSignedInThenObservableFrom(userId -> eventRepository.events(userId)
                .map(events -> filter(events, Event::getFavorited))
                .map(events -> {
                    Collections.sort(events, byStartDate());
                    return events;
                })
                .take(1)
                .subscribeOn(Schedulers.io()));
    }

    private Comparator<Event> byStartDate() {
        return (event1, event2) -> event1.getStartTime().compareTo(event2.getStartTime());
    }
}
