package net.squanchy.search

import net.squanchy.search.domain.view.SearchListElement

sealed class SearchResult {

    data class Success(val elements: List<SearchListElement>) : SearchResult() {
        val isEmpty: Boolean
            get() = elements.isEmpty()
    }

    object Error : SearchResult()

    companion object {

        private val emptySearch = SearchResult.Success(emptyList())

        fun empty(): SearchResult = emptySearch
    }
}
