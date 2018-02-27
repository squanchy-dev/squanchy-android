package net.squanchy.schedule.domain.view

import net.squanchy.support.lang.Optional

fun aTrack(
    id: String = "a track id",
    name: String = "a track name",
    accentColor: Optional<String> = Optional.of("#ABCDEF"),
    textColor: Optional<String> = Optional.of("#FEDCBA"),
    iconUrl: Optional<String> = Optional.of("www.squanchy.net")
) = Track(
    id = id,
    name = name,
    accentColor = accentColor,
    textColor = textColor,
    iconUrl = iconUrl
)
