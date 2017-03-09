package net.squanchy.search.view;

import net.squanchy.search.SearchResults;
import net.squanchy.speaker.domain.view.Speaker;

import static net.squanchy.search.view.SpeakerAdapter.ViewTypeId;

class ItemsAdapter {

    private final SearchResults searchResults;

    ItemsAdapter(SearchResults searchResults) {
        this.searchResults = searchResults;
    }

    int totalItemsCount() {
        if (searchResults.isEmpty()) {
            return 0;
        }

        int eventsCount = searchResults.events().size();
        int eventsHeadersCount = headersCountForSectionItemsCount(eventsCount);

        int speakersCount = searchResults.speakers().size();
        int speakersHeadersCount = headersCountForSectionItemsCount(speakersCount);

        return eventsHeadersCount + eventsCount + speakersHeadersCount + speakersCount;
    }

    private static int headersCountForSectionItemsCount(int itemsCount) {
        return itemsCount > 0 ? 1 : 0;
    }

    boolean isEmpty() {
        return searchResults.isEmpty();
    }

    int viewTypeAtAbsolutePosition(int position) {
        return ViewTypeId.SPEAKER; // TODO
    }

    long itemIdAtAbsolutePosition(int position) {
        return 0;                // TODO
    }

    Speaker speakerAtAbsolutePosition(int position) {
        return null;             // TODO
    }

    int headerTextAtAbsolutePosition(int position) {
        return 0;                // TODO
    }
}
