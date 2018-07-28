package net.squanchy.search.view

import androidx.recyclerview.widget.GridLayoutManager
import net.squanchy.search.SearchListElement

internal class GridSpanSizeLookup(
    private val itemRetriever: (Int) -> SearchListElement,
    private val isAdapterEmpty: () -> Boolean,
    private val columnCount: Int
) : GridLayoutManager.SpanSizeLookup() {

    init {
        super.setSpanIndexCacheEnabled(true)
    }

    override fun getSpanSize(position: Int): Int {
        if (isAdapterEmpty()) {
            return SINGLE_COLUMN_SPAN_SIZE
        } else {
            return getSpanSizeFor(itemRetriever(position))
        }
    }

    private fun getSpanSizeFor(element: SearchListElement): Int =
        when (element) {
            is SearchListElement.EventHeader -> columnCount
            is SearchListElement.SpeakerHeader -> columnCount
            is SearchListElement.EventElement -> columnCount
            is SearchListElement.SpeakerElement -> SINGLE_COLUMN_SPAN_SIZE
            is SearchListElement.AlgoliaLogo -> columnCount
        }

    companion object {
        private const val SINGLE_COLUMN_SPAN_SIZE = 1
    }
}
