package net.squanchy.search

import net.squanchy.search.domain.view.SearchListResult

sealed class SearchResult {

    data class Success(val result: SearchListResult) : SearchResult() {
        val isEmpty: Boolean
            get() = result.elements.isEmpty()
    }

    object Error : SearchResult()

    companion object {

        private val emptySearch = SearchResult.Success(SearchListResult(emptyList()))

        fun empty(): SearchResult = emptySearch
    }
}
