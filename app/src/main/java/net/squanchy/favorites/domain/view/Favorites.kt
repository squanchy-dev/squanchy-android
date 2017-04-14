package net.squanchy.favorites.domain.view

import net.squanchy.schedule.domain.view.Event

data class Favorites(val events: List<Event>) {

    companion object {

        fun create(events: List<Event>) = Favorites(events)
    }
}
