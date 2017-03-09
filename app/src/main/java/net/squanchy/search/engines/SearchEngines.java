package net.squanchy.search.engines;

import net.squanchy.schedule.domain.view.Event;

public final class SearchEngines {

    private static final EventSearchEngine EVENT_SEARCH_ENGINE = new EventSearchEngine();

    private SearchEngines() {
        // Not instantiable
    }

    public static SearchEngine<Event> forEvents() {
        return EVENT_SEARCH_ENGINE;
    }
}
