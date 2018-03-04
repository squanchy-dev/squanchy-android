package net.squanchy.search

import net.squanchy.schedule.domain.view.Event
import net.squanchy.speaker.domain.view.Speaker

data class SearchResults(
        val events: List<Event>,
        val speakers: List<Speaker>
) {
    val isEmpty: Boolean
        get() = events.isEmpty() && speakers.isEmpty()

    companion object {

        fun create(events: List<Event>, speakers: List<Speaker>) = SearchResults(events, speakers)
    }
}
