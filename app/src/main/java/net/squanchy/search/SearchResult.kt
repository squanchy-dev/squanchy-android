package net.squanchy.search

import net.squanchy.schedule.domain.view.Event
import net.squanchy.speaker.domain.view.Speaker

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

sealed class SearchListElement {
    object EventHeader : SearchListElement()
    data class EventElement(val event: Event) : SearchListElement()
    object SpeakerHeader : SearchListElement()
    data class SpeakerElement(val speaker: Speaker) : SearchListElement()
    object AlgoliaLogo : SearchListElement()
}
