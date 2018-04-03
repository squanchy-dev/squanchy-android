package net.squanchy.search.view;

import java.util.List;
import java.util.Locale;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.search.SearchResult;
import net.squanchy.search.view.SearchAdapter.ViewTypeId;
import net.squanchy.speaker.domain.view.Speaker;

class ItemsAdapter {

    // These values are "random", we count on them not clashing with the other
    // random values that are used for non-hardcoded numeric IDs (for events
    // and speakers). This is a reasonable assumption in the Long range.
    // In addition, the CRC32 values we use as numeric IDs are always positive.
    private static final long ITEM_ID_EVENTS_HEADER = -100;
    private static final long ITEM_ID_SPEAKERS_HEADER = -101;

    private final SearchResult.Success searchResult;

    ItemsAdapter(SearchResult.Success searchResult) {
        this.searchResult = searchResult;
    }

    int totalItemsCount() {
        if (searchResult.isEmpty()) {
            return 0;
        }

        int totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResult.getEvents());
        int totalSpeakerItemsCount = totalCountForSectionIncludingHeaders(searchResult.getSpeakers());

        return totalEventItemsCount + totalSpeakerItemsCount;
    }

    boolean isEmpty() {
        return searchResult.isEmpty();
    }

    @ViewTypeId
    int viewTypeAtAbsolutePosition(int position) {
        ensurePositionExists(position);

        int totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResult.getEvents());

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

        int totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResult.getEvents());
        if (totalEventItemsCount > 0 && position < totalEventItemsCount) {
            if (position == 0) {
                return ITEM_ID_EVENTS_HEADER;
            } else {
                return searchResult.getEvents().get(position - 1).getNumericId();
            }
        } else {
            int adjustedPosition = position - totalEventItemsCount;
            // We checked position is in range so it MUST be a speaker
            if (adjustedPosition == 0) {
                return ITEM_ID_SPEAKERS_HEADER;
            } else {
                return searchResult.getSpeakers().get(adjustedPosition - 1).getNumericId();
            }
        }
    }

    Speaker speakerAtAbsolutePosition(int position) {
        ensurePositionExists(position);

        int totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResult.getEvents());
        if (totalEventItemsCount > 0 && position < totalEventItemsCount) {
            throw new IndexOutOfBoundsException("No speaker at position " + position + ", that is supposed to be in the events sublist");
        }

        int adjustedPosition = position - totalEventItemsCount;
        // We checked position is in range so it MUST be a speaker
        if (adjustedPosition > 0) {
            return searchResult.getSpeakers().get(adjustedPosition - 1);
        } else {
            throw new IndexOutOfBoundsException("No speaker at position " + position + ", that is supposed to be the speakers header");
        }
    }

    Event eventAtAbsolutePosition(int position) {
        ensurePositionExists(position);

        int totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResult.getEvents());
        if (position == 0) {
            throw new IndexOutOfBoundsException("No event at position " + position + ", that is supposed to be the events header");
        } else if (position - 1 >= totalEventItemsCount) {
            throw new IndexOutOfBoundsException("No event at position " + position + ", that is supposed to be in the speakers sublist");
        }

        return searchResult.getEvents().get(position - 1);
    }

    HeaderType headerTypeAtAbsolutePosition(int position) {
        ensurePositionExists(position);

        int totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResult.getEvents());
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
        int totalItemsCount = totalItemsCount();
        if (position < 0 || position >= totalItemsCount) {
            String message = String.format(Locale.ROOT, "Position %1$d is not valid, must be [0, %2$d)", position, totalItemsCount);
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
