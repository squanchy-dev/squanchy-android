package net.squanchy.schedule.domain.view

import arrow.core.None
import arrow.core.Option

data class Track(
    val id: String,
    val numericId: Long,
    val name: String,
    val accentColor: Option<String> = None,
    val textColor: Option<String> = None,
    val iconUrl: Option<String> = None
)
