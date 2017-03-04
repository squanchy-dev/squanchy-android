package net.squanchy.search.view;

import android.support.v7.widget.GridLayoutManager;

import java.util.Locale;

import net.squanchy.search.view.SpeakerAdapter.ViewTypeId;

class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private static final int SINGLE_COLUMN_SPAN_SIZE = 1;

    private final int columnCount;
    private final ItemsAdapter itemsAdapter;

    GridSpanSizeLookup(ItemsAdapter adapter, int columnCount) {
        this.itemsAdapter = adapter;
        this.columnCount = columnCount;
    }

    @Override
    public int getSpanSize(int position) {
        if (itemsAdapter.isEmpty()) {
            return SINGLE_COLUMN_SPAN_SIZE;
        }

        if (position < 0 || position >= itemsAdapter.getTotalItemsCount()) {
            String message = String.format(Locale.UK, "Position %d is not valid, must be [0, totalItemsCount)", position);
            throw new IndexOutOfBoundsException(message);
        }

        @ViewTypeId
        int itemViewType = itemsAdapter.getViewTypeAt(position);
        return getSizeFor(itemViewType);
    }

    private int getSizeFor(@ViewTypeId int viewTypeId) {
        if (viewTypeId == ViewTypeId.HEADER) {
            return columnCount;
        } else {
            return SINGLE_COLUMN_SPAN_SIZE;
        }
    }
}
