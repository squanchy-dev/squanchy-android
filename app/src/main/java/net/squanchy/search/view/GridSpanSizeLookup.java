package net.squanchy.search.view;

import android.support.v7.widget.GridLayoutManager;

import net.squanchy.search.view.SearchAdapter.ViewTypeId;

class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private static final int SINGLE_COLUMN_SPAN_SIZE = 1;

    private final int columnCount;
    private final ItemsAdapter itemsAdapter;

    GridSpanSizeLookup(ItemsAdapter adapter, int columnCount) {
        this.itemsAdapter = adapter;
        this.columnCount = columnCount;

        super.setSpanIndexCacheEnabled(true);
    }

    @Override
    public int getSpanSize(int position) {
        if (itemsAdapter.isEmpty()) {
            return SINGLE_COLUMN_SPAN_SIZE;
        }

        return getSpanSizeFor(itemsAdapter.viewTypeAtAbsolutePosition(position));
    }

    private int getSpanSizeFor(@ViewTypeId int viewTypeId) {
        if (viewTypeId == ViewTypeId.HEADER || viewTypeId == ViewTypeId.EVENT) {
            return columnCount;
        } else {
            return SINGLE_COLUMN_SPAN_SIZE;
        }
    }
}
