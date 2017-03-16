package net.squanchy.eventdetails;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.EventRepository;

import io.reactivex.Completable;
import io.reactivex.Observable;

class EventDetailsService {

    private final EventRepository eventRepository;
    private final FirebaseAuthService authService;

    EventDetailsService(EventRepository eventRepository, FirebaseAuthService authService) {
        this.eventRepository = eventRepository;
        this.authService = authService;
    }

    public Observable<Event> event(String eventId) {
        return authService.signInAndObserve(userId -> eventRepository.event(eventId, userId));
    }

    Completable favorite(String eventId) {
        return authService.signInAndComplete(userId -> eventRepository.addFavorite(eventId, userId));
    }

    Completable removeFavorite(String eventId) {
        return authService.signInAndComplete(userId -> eventRepository.removeFavorite(eventId, userId));
    }
}
