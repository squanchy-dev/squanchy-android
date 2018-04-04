package net.squanchy.search.view

import android.support.v7.widget.GridLayoutManager

import net.squanchy.search.view.SearchAdapter.ViewTypeId

internal class GridSpanSizeLookup(private val itemsAdapter: ItemsAdapter, private val columnCount: Int) : GridLayoutManager.SpanSizeLookup() {

    init {
        super.setSpanIndexCacheEnabled(true)
    }

    override fun getSpanSize(position: Int): Int {
        return if (itemsAdapter.isEmpty) SINGLE_COLUMN_SPAN_SIZE else getSpanSizeFor(itemsAdapter.viewTypeAtAbsolutePosition(position))
    }

    private fun getSpanSizeFor(@ViewTypeId viewTypeId: Int): Int =
        when (viewTypeId) {
            ViewTypeId.HEADER -> columnCount
            ViewTypeId.EVENT -> columnCount
            ViewTypeId.SPEAKER -> SINGLE_COLUMN_SPAN_SIZE
            else -> error("Invalid ViewTypeId $viewTypeId")
        }

    companion object {
        private const val SINGLE_COLUMN_SPAN_SIZE = 1
    }
}
