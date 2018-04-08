package net.squanchy.search.view

import android.support.v7.widget.GridLayoutManager
import net.squanchy.search.SearchListElement

internal class GridSpanSizeLookup(private val items: List<SearchListElement>, private val columnCount: Int) : GridLayoutManager.SpanSizeLookup() {

    init {
        super.setSpanIndexCacheEnabled(true)
    }

    override fun getSpanSize(position: Int): Int {
        return if (items.isEmpty()) SINGLE_COLUMN_SPAN_SIZE else getSpanSizeFor(items[position])
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
