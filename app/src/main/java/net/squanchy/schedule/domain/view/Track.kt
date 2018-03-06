package net.squanchy.schedule.domain.view

import net.squanchy.support.lang.Optional

data class Track(
    val id: String,
    val name: String,
    val accentColor: Optional<String> = Optional.absent(),
    val textColor: Optional<String> = Optional.absent(),
    val iconUrl: Optional<String> = Optional.absent()
)
