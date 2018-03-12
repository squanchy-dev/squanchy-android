package net.squanchy.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.search.engines.SearchEngines;
import net.squanchy.service.firestore.FirebaseAuthService;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.service.repository.SpeakerRepository;
import net.squanchy.speaker.domain.view.Speaker;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static net.squanchy.support.lang.Lists.filter;

class SearchService {

    private final EventRepository eventRepository;
    private final SpeakerRepository speakerRepository;
    private final SearchEngines searchEngines;
    private final FirebaseAuthService authService;

    SearchService(
            EventRepository eventRepository,
            SpeakerRepository speakerRepository,
            SearchEngines searchEngines,
            FirebaseAuthService authService
    ) {
        this.eventRepository = eventRepository;
        this.speakerRepository = speakerRepository;
        this.searchEngines = searchEngines;
        this.authService = authService;
    }

    Observable<SearchResults> find(String query) {
        return authService.ifUserSignedInThenObservableFrom(userId -> Observable.combineLatest(
                findEvents(query, userId),
                findSpeakers(query),
                SearchResults.Companion::create
        ));
    }

    private Observable<List<Event>> findEvents(String query, String userId) {
        return eventRepository.events(userId)
                .map(onlyEventsMatching(query));
    }

    private Function<List<Event>, List<Event>> onlyEventsMatching(String query) {
        return events -> filter(events, event -> searchEngines.forEvents().matches(event, query));
    }

    private Observable<List<Speaker>> findSpeakers(String query) {
        return speakerRepository.speakers()
                .map(onlySpeakersMatching(query));
    }

    private Function<List<Speaker>, List<Speaker>> onlySpeakersMatching(String query) {
        return speakers -> filter(speakers, speaker -> searchEngines.forSpeakers().matches(speaker, query));
    }

    public Observable<List<Speaker>> speakers() {
        return speakerRepository.speakers()
                .map(sortedByName());
    }

    private Function<List<Speaker>, List<Speaker>> sortedByName() {
        return speakers -> {
            ArrayList<Speaker> sortedSpeakers = new ArrayList<>(speakers);
            Collections.sort(sortedSpeakers, (speaker1, speaker2) -> speaker1.getName().compareToIgnoreCase(speaker2.getName()));
            return sortedSpeakers;
        };
    }
}
