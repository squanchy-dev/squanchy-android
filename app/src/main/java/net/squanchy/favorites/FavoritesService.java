package net.squanchy.favorites;

import net.squanchy.favorites.domain.view.Favorites;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.EventRepository;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static net.squanchy.support.lang.Lists.filter;

class FavoritesService {

    private final FirebaseAuthService authService;
    private final EventRepository eventRepository;

    FavoritesService(FirebaseAuthService authService, EventRepository eventRepository) {
        this.authService = authService;
        this.eventRepository = eventRepository;
    }

    public Observable<Favorites> favorites() {
        return authService.ifUserSignedInThenObservableFrom(userId -> eventRepository.events(userId)
                .map(events -> filter(events, Event::favorited))
                .map(Favorites::create)
                .subscribeOn(Schedulers.io()));
    }
}
