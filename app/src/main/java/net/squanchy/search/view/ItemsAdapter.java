package net.squanchy.search.view;

import java.util.List;
import java.util.Locale;

import net.squanchy.search.SearchResults;
import net.squanchy.search.view.SpeakerAdapter.ViewTypeId;
import net.squanchy.speaker.domain.view.Speaker;

class ItemsAdapter {

    private final SearchResults searchResults;

    ItemsAdapter(SearchResults searchResults) {
        this.searchResults = searchResults;
    }

    int totalItemsCount() {
        if (searchResults.isEmpty()) {
            return 0;
        }

        int totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResults.events());
        int totalSpeakerItemsCount = totalCountForSectionIncludingHeaders(searchResults.speakers());

        return totalEventItemsCount + totalSpeakerItemsCount;
    }

    boolean isEmpty() {
        return searchResults.isEmpty();
    }

    @ViewTypeId
    int viewTypeAtAbsolutePosition(int position) {
        ensurePositionExists(position);

        int totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResults.events());

        int adjustedPosition;
        if (position < totalEventItemsCount) {
            adjustedPosition = position;
        } else {
            adjustedPosition = position - totalEventItemsCount;
        }

        if (adjustedPosition == 0) {
            return ViewTypeId.HEADER;
        } else if (totalEventItemsCount > 0) {
            return ViewTypeId.EVENT;
        } else {
            return ViewTypeId.SPEAKER;
        }
    }

    private int totalCountForSectionIncludingHeaders(List sectionItems) {
        int eventsCount = sectionItems.size();
        return eventsCount + headersCountForSectionItemsCount(eventsCount);
    }

    private static int headersCountForSectionItemsCount(int itemsCount) {
        return itemsCount > 0 ? 1 : 0;
    }

    private void ensurePositionExists(int position) {
        if (position < 0 || position >= totalItemsCount()) {
            String message = String.format(Locale.ROOT, "Position %d is not valid, must be [0, totalItemsCount)", position);
            throw new IndexOutOfBoundsException(message);
        }
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
