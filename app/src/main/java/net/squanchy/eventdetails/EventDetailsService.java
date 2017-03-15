package net.squanchy.eventdetails;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.repository.EventRepository;

import io.reactivex.Observable;

class EventDetailsService {

    private final EventRepository eventRepository;

    EventDetailsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Observable<Event> event(String eventId) {
        return eventRepository.event(eventId);
    }

    void favorite(String eventId) {
        eventRepository.favorite(eventId);
    }

    void removeFavorite(String eventId) {
        eventRepository.removeFavorite(eventId);
    }
}
