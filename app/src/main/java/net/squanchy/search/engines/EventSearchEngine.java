package net.squanchy.search.engines;

import net.squanchy.schedule.domain.view.Event;

class EventSearchEngine implements SearchEngine<Event> {

    private static final int MIN_QUERY_LENGTH = 2;

    @Override
    public boolean matches(Event event, String query) {
        return queryIsLongEnough(query)
                && eventIsSearchable(event)
                && matchesQuery(event, query);
    }

    private boolean queryIsLongEnough(String query) {
        return query.length() >= MIN_QUERY_LENGTH;
    }

    private boolean eventIsSearchable(Event event) {
        return event.type() != Event.Type.LUNCH
                && event.type() != Event.Type.COFFEE_BREAK
                && event.type() != Event.Type.REGISTRATION;
    }

    private boolean matchesQuery(Event event, String query) {
        String normalizedQuery = StringNormalizer.normalize(query);
        String normalizedTitle = StringNormalizer.normalize(event.title());

        return normalizedTitle.contains(normalizedQuery);
    }
}
