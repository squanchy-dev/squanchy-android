package net.squanchy.search.view;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.search.model.TitledList;
import net.squanchy.speaker.domain.view.Speaker;

import static net.squanchy.search.view.SpeakerAdapter.ViewTypeId;

class ItemsAdapter {

    private final List<TitledList<Speaker>> lists = new ArrayList<>();

    int getTotalItemsCount() {
        int size = 0;
        for (TitledList list : lists) {
            size += list.size();
        }
        return size + getAdditionalItemsCount();
    }

    private int getAdditionalItemsCount() {
        return lists.size();
    }

    boolean isEmpty() {
        return lists.isEmpty();
    }

    void addItems(TitledList<Speaker> list) {
        lists.add(list);
    }

    int getViewTypeAt(int position) {
        if (position == 0) {
            return ViewTypeId.HEADER;
        }
        return ViewTypeId.SPEAKER;
    }
}
