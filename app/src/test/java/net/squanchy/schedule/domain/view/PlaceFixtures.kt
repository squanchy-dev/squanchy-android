package net.squanchy.schedule.domain.view

import net.squanchy.support.lang.Optional

fun aPlace(
    id: String = "banana-room",
    name: String = "The banana room™",
    floor: Optional<String> = Optional.of("Banana floor")
) = Place(
    id = id,
    name = name,
    floor = floor
)
