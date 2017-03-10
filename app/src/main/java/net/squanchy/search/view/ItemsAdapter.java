package net.squanchy.search.view;

import java.util.List;
import java.util.Locale;

import net.squanchy.search.SearchResults;
import net.squanchy.search.view.SpeakerAdapter.ViewTypeId;
import net.squanchy.speaker.domain.view.Speaker;

class ItemsAdapter {

    // These values are "random", we count on them not clashing with the other
    // random values that are used for non-hardcoded numeric IDs (for events
    // and speakers). This is a reasonable assumption in the Long range.
    private static final long ITEM_ID_EVENTS_HEADER = 100;
    private static final long ITEM_ID_SPEAKERS_HEADER = 101;

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
        } else if (position < totalEventItemsCount) {
            return ViewTypeId.EVENT;
        } else {
            return ViewTypeId.SPEAKER;
        }
    }

    long itemIdAtAbsolutePosition(int position) {
        ensurePositionExists(position);

        int totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResults.events());
        if (totalEventItemsCount > 0 && position < totalEventItemsCount) {
            if (position == 0) {
                return ITEM_ID_EVENTS_HEADER;
            } else {
                return searchResults.events().get(position - 1).numericId();
            }
        } else {
            int adjustedPosition = position - totalEventItemsCount;
            // We checked position is in range so it MUST be a speaker
            if (adjustedPosition == 0) {
                return ITEM_ID_SPEAKERS_HEADER;
            } else {
                return searchResults.speakers().get(adjustedPosition - 1).numericId();
            }
        }
    }

    Speaker speakerAtAbsolutePosition(int position) {
        ensurePositionExists(position);

        int totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResults.events());
        if (totalEventItemsCount > 0 && position < totalEventItemsCount) {
            throw new IndexOutOfBoundsException("No speaker at position " + position + ", that is supposed to be in the events sublist");
        }

        int adjustedPosition = position - totalEventItemsCount;
        // We checked position is in range so it MUST be a speaker
        if (adjustedPosition > 0) {
            return searchResults.speakers().get(adjustedPosition - 1);
        } else {
            throw new IndexOutOfBoundsException("No speaker at position " + position + ", that is supposed to be the speakers header");
        }
    }

    HeaderType headerTypeAtAbsolutePosition(int position) {
        ensurePositionExists(position);

        int totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResults.events());
        if (totalEventItemsCount > 0) {
            if (position == 0) {
                return HeaderType.EVENTS;
            } else if (position < totalEventItemsCount) {
                throw new IndexOutOfBoundsException("No header at position " + position + ", that is supposed to be an event");
            }
        }

        int adjustedPosition = position - totalEventItemsCount;
        // We checked position is in range so it MUST be a speaker
        if (adjustedPosition == 0) {
            return HeaderType.SPEAKERS;
        } else {
            throw new IndexOutOfBoundsException("No header at position " + position + ", that is supposed to be a speaker");
        }
    }

    private void ensurePositionExists(int position) {
        if (position < 0 || position >= totalItemsCount()) {
            String message = String.format(Locale.ROOT, "Position %d is not valid, must be [0, totalItemsCount)", position);
            throw new IndexOutOfBoundsException(message);
        }
    }

    private int totalCountForSectionIncludingHeaders(List sectionItems) {
        int eventsCount = sectionItems.size();
        return eventsCount + headersCountForSectionItemsCount(eventsCount);
    }

    private static int headersCountForSectionItemsCount(int itemsCount) {
        return itemsCount > 0 ? 1 : 0;
    }
}
