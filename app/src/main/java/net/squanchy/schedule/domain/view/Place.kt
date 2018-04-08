package net.squanchy.schedule.domain.view

import arrow.core.Option

data class Place(
    val id: String,
    val name: String,
    val floor: Option<String>
)
