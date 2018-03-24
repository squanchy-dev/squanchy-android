package net.squanchy.schedule.domain.view

import org.joda.time.LocalDate

data class SchedulePage(
    val dayId: String,
    val date: LocalDate,
    val position: Int,
    val events: List<Event>
)
