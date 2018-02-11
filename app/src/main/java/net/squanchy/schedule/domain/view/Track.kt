package net.squanchy.schedule.domain.view

import net.squanchy.support.lang.Optional

data class Track(
        val id: String,
        val name: String,
        val accentColor: Optional<String>,
        val textColor: Optional<String>,
        val iconUrl: Optional<String>
)
