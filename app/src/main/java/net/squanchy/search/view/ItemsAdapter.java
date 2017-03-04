package net.squanchy.search.view;

import java.util.List;

import net.squanchy.speaker.domain.view.Speaker;

import static net.squanchy.search.view.SpeakerAdapter.ViewTypeId;

class ItemsAdapter {

    private static final int NUMBER_OF_TITLES = 1;
    private List<Speaker> items;

    ItemsAdapter(List<Speaker> items) {
        this.items = items;
    }

    int getTotalItemsCount() {
        return items.size() + getAdditionalItemsCount();
    }

    private int getAdditionalItemsCount() {
        return NUMBER_OF_TITLES;
    }

    boolean isEmpty() {
        return items.isEmpty();
    }

    void setItems(List<Speaker> items) {
        this.items = items;
    }

    int getViewTypeAt(int position) {
        if (position == 0) {
            return ViewTypeId.HEADER;
        }
        return ViewTypeId.SPEAKER;
    }
}
