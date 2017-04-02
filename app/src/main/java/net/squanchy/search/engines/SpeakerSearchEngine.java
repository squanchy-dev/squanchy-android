package net.squanchy.search.engines;

import net.squanchy.speaker.domain.view.Speaker;

class SpeakerSearchEngine implements SearchEngine<Speaker> {

    private static final int MIN_QUERY_LENGTH = 2;

    @Override
    public boolean matches(Speaker speaker, String query) {
        return queryIsLongEnough(query)
                && matchesQuery(speaker, query);
    }

    private boolean queryIsLongEnough(String query) {
        return query.length() >= MIN_QUERY_LENGTH;
    }

    private boolean matchesQuery(Speaker speaker, String query) {
        String normalizedQuery = StringNormalizer.normalize(query);
        String normalizedTitle = StringNormalizer.normalize(speaker.getName());

        return normalizedTitle.contains(normalizedQuery);
    }
}
