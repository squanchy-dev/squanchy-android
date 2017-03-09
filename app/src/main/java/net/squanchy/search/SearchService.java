package net.squanchy.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.service.repository.SpeakerRepository;
import net.squanchy.speaker.domain.view.Speaker;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static net.squanchy.support.lang.Lists.filter;

class SearchService {

    private final EventRepository eventRepository;
    private final SpeakerRepository speakerRepository;

    SearchService(EventRepository eventRepository, SpeakerRepository speakerRepository) {
        this.eventRepository = eventRepository;
        this.speakerRepository = speakerRepository;
    }

    public Observable<List<Event>> findEvents(String query) {
        return eventRepository.events()
                .map(onlyEventsMatching(query));
    }

    private Function<List<Event>, List<Event>> onlyEventsMatching(String query) {
        return events -> filter(events, event -> matchesQuery(event, query) && eventIsSearchable(event));
    }

    private boolean matchesQuery(Event event, String query) {
        return event.title().contains(query);
    }

    private boolean eventIsSearchable(Event event) {
        return event.type() != Event.Type.LUNCH
                && event.type() != Event.Type.COFFEE_BREAK
                && event.type() != Event.Type.REGISTRATION;
    }

    public Observable<List<Speaker>> findSpeakers(String query) {
        return speakerRepository.speakers()
                .map(onlySpeakersMatching(query));
    }

    private Function<List<Speaker>, List<Speaker>> onlySpeakersMatching(String query) {
        return speakers -> filter(speakers, speaker -> speaker.name().contains(query));
    }

    public Observable<List<Speaker>> speakers() {
        return speakerRepository.speakers()
                .map(sortedByName());
    }

    private Function<List<Speaker>, List<Speaker>> sortedByName() {
        return speakers -> {
            ArrayList<Speaker> sortedSpeakers = new ArrayList<>(speakers);
            Collections.sort(sortedSpeakers, (speaker1, speaker2) -> speaker1.name().compareToIgnoreCase(speaker2.name()));
            return sortedSpeakers;
        };
    }
}
