package net.squanchy.schedule.domain.view

import org.threeten.bp.LocalDate

data class SchedulePage(
    val dayId: String,
    val date: LocalDate,
    val events: List<Event>
)
