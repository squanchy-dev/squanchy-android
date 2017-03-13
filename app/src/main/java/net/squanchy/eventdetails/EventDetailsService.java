package net.squanchy.eventdetails;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.repository.EventRepository;

import org.joda.time.DateTime;

import io.reactivex.Observable;

class EventDetailsService {

    private final EventRepository eventRepository;

    EventDetailsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Observable<Event> event(String eventId) {
        return eventRepository.event(eventId);
    }
}
