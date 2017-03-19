package net.squanchy.eventdetails;

import com.google.firebase.auth.FirebaseUser;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

class EventDetailsService {

    private final EventRepository eventRepository;
    private final FirebaseAuthService authService;

    EventDetailsService(EventRepository eventRepository, FirebaseAuthService authService) {
        this.eventRepository = eventRepository;
        this.authService = authService;
    }

    public Observable<Event> event(String eventId) {
        return authService.ifUserSignedInThenObservableFrom(userId -> eventRepository.event(eventId, userId));
    }

    Single<FavoriteResult> toggleFavorite(Event event) {
        return currentUser()
                .flatMap(optionalUser -> {
                    if (!optionalUser.isPresent() || optionalUser.get().isAnonymous()) {
                        return Single.just(FavoriteResult.MUST_AUTHENTICATE);
                    }

                    return toggleFavoriteOn(event)
                            .andThen(Single.just(FavoriteResult.SUCCESS));
                });
    }

    private Completable toggleFavoriteOn(Event event) {
        if (event.favorited()) {
            return removeFavorite(event.id());
        } else {
            return favorite(event.id());
        }
    }

    private Completable removeFavorite(String eventId) {
        return authService.ifUserSignedInThenCompletableFrom(userId -> eventRepository.removeFavorite(eventId, userId));
    }

    private Completable favorite(String eventId) {
        return authService.ifUserSignedInThenCompletableFrom(userId -> eventRepository.addFavorite(eventId, userId));
    }

    private Single<Optional<FirebaseUser>> currentUser() {
        return authService.currentUser().firstOrError();
    }

    enum FavoriteResult {
        SUCCESS,
        MUST_AUTHENTICATE
    }
}
