package net.squanchy.service.repository;

import java.util.List;

import net.squanchy.schedule.domain.view.Event;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface EventRepository {

    Observable<Event> event(String eventId, String userId);

    Observable<List<Event>> events(String userId);

    Completable addFavorite(String eventId, String userId);

    Completable removeFavorite(String eventId, String userId);
}
