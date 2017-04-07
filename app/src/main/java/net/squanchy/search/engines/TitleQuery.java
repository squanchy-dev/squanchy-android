package net.squanchy.search.engines;

import net.squanchy.schedule.domain.view.Event;

class TitleQuery implements Query {

    @Override
    public boolean matches(Event event, String query) {
        String normalizedQuery = StringNormalizer.normalize(query);
        String normalizedTitle = StringNormalizer.normalize(event.title());

        return normalizedTitle.contains(normalizedQuery);
    }
}
