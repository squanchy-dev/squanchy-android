package net.squanchy.search.engines;

import java.util.Arrays;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.support.lang.Lists;

class TextQuery implements Query {

    @Override
    public boolean matches(Event event, String query) {
        if (!event.description().isPresent()) {
            return false;
        }

        String[] normalizedQueries = StringNormalizer.normalize(query).split("\\s");
        String normalizedText = StringNormalizer.normalize(event.description().get());

        return Lists.all(Arrays.asList(normalizedQueries), normalizedText::contains);
    }
}
