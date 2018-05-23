package net.squanchy.schedule.domain.view

import arrow.core.Option
import java.util.Locale

enum class ExperienceLevel(
    private val rawLevel: String
) {
    BEGINNER("beginner"),
    INTERMEDIATE("intermediate"),
    ADVANCED("advanced");

    companion object {

        fun tryParsingFrom(rawLevel: String?): Option<ExperienceLevel> =
            rawLevel?.let {
                try {
                    Option(fromRawLevel(it))
                } catch (e: IllegalArgumentException) {
                    null
                }
            } ?: Option.empty()

        private fun fromRawLevel(rawLevel: String) =
            when (rawLevel.toLowerCase(Locale.US)) {
                BEGINNER.rawLevel -> BEGINNER
                INTERMEDIATE.rawLevel -> INTERMEDIATE
                ADVANCED.rawLevel -> ADVANCED
                else -> throw IllegalArgumentException("Invalid raw level description: $rawLevel")
            }
    }
}
