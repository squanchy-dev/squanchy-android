package net.squanchy.schedule.domain.view

import arrow.core.Option
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.speaker.domain.view.Speaker
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

data class Event(
    val id: String,
    val numericId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val title: String,
    val place: Option<Place>,
    val track: Option<Track>,
    val speakers: List<Speaker>,
    val experienceLevel: Option<ExperienceLevel>,
    val type: Type,
    val favorite: Boolean,
    val description: Option<String>,
    val timeZone: ZoneId
) {

    val zonedStartTime: ZonedDateTime
        get() = startTime.atZone(timeZone)

    override fun toString(): String = "Event [id: '$id', title: '$title', startTime: '$startTime']"

    enum class Type(private val rawType: String) {
        REGISTRATION("registration"),
        TALK("talk"),
        KEYNOTE("keynote"),
        COFFEE_BREAK("coffee_break"),
        LUNCH("lunch"),
        SOCIAL("social"),
        OTHER("other"),
        WORKSHOP("workshop");

        companion object {

            fun fromRawType(rawType: String): Type {
                return values().find { it.rawType.equals(rawType, ignoreCase = true) }
                    ?: throw IllegalArgumentException("Unsupported raw event type: $rawType")
            }
        }
    }
}
