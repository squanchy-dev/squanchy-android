package net.squanchy.search.engines;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.speaker.domain.view.Speaker;

public class SearchEngines {

    private static final SearchEngine<Event> EVENT_SEARCH_ENGINE = new EventSearchEngine();
    private static final SearchEngine<Speaker> SPEAKER_SEARCH_ENGINE = new SpeakerSearchEngine();

    public SearchEngine<Event> forEvents() {
        return EVENT_SEARCH_ENGINE;
    }

    public SearchEngine<Speaker> forSpeakers() {
        return SPEAKER_SEARCH_ENGINE;
    }
}
