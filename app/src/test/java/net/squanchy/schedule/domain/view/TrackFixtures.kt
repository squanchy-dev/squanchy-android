package net.squanchy.schedule.domain.view

import net.squanchy.support.lang.Optional

fun aTrack(
    id: String = "anId",
    name: String = "a name",
    accentColor: Optional<String> = Optional.absent(),
    textColor: Optional<String> = Optional.absent(),
    iconUrl: Optional<String> = Optional.absent()
) = Track(
    id = id,
    name = name,
    accentColor = accentColor,
    textColor = textColor,
    iconUrl = iconUrl
)
