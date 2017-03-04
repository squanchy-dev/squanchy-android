package net.squanchy.search.view;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.search.model.TitledList;
import net.squanchy.speaker.domain.view.Speaker;

import static net.squanchy.search.view.SpeakerAdapter.ViewTypeId;

class ItemsAdapter {

    private final List<TitledList<Speaker>> items = new ArrayList<>();

    int getTotalItemsCount() {
        int size = 0;
        for (TitledList list : items) {
            size += list.getItems().size();
        }
        return size + getAdditionalItemsCount();
    }

    private int getAdditionalItemsCount() {
        return items.size();
    }

    boolean isEmpty() {
        return items.isEmpty();
    }

    void addItems(TitledList<Speaker> list) {
        items.add(list);
    }

    int getViewTypeAt(int position) {
        if (position == 0) {
            return ViewTypeId.HEADER;
        }
        return ViewTypeId.SPEAKER;
    }
}
