package net.squanchy.schedule.domain.view

import net.squanchy.support.lang.Optional

data class Track(
    val id: String,
    val name: String,
    val accentColor: Optional<String>,
    val textColor: Optional<String>,
    val iconUrl: Optional<String>
) {
    companion object {

        fun create(
            id: String,
            name: String,
            accentColor: Optional<String>,
            textColor: Optional<String>,
            iconUrl: Optional<String>
        ) = Track(
                id,
                name,
                accentColor,
                textColor,
                iconUrl
        )
    }
}
