package net.squanchy.search

import net.squanchy.schedule.domain.view.Event
import net.squanchy.speaker.domain.view.Speaker

sealed class SearchResult {

    data class Success(val events: List<Event>, val speakers: List<Speaker>) : SearchResult() {
        val isEmpty: Boolean
            get() = events.isEmpty() && speakers.isEmpty()
    }

    object Error : SearchResult()
}
