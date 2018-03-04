package net.squanchy.schedule.domain.view

import net.squanchy.support.lang.Optional

data class Place(
    val id: String,
    val name: String,
    val floor: Optional<String>
) {
    companion object {
        fun create(id: String, name: String, floor: Optional<String>) = Place(id, name, floor)
    }
}
