package net.squanchy.schedule.domain.view

import org.joda.time.LocalDateTime

data class SchedulePage(
    val dayId: String,
    val date: LocalDateTime,
    val events: List<Event>
) {
    companion object {

        fun create(
            dayId: String,
            date: LocalDateTime,
            events: List<Event>
        ) = SchedulePage(
                dayId,
                date,
                events
        )
    }
}
