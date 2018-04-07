package net.squanchy.schedule.domain.view

import arrow.core.Option

data class Track(
    val id: String,
    val numericId: Long,
    val name: String,
    val accentColor: Option<String> = Option.empty(),
    val textColor: Option<String> = Option.empty(),
    val iconUrl: Option<String> = Option.empty()
)
