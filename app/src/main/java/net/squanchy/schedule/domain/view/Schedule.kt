package net.squanchy.schedule.domain.view

import org.threeten.bp.ZoneId

data class Schedule(val pages: List<SchedulePage>, val timeZone: ZoneId) {

    val isEmpty: Boolean
        get() = pages.all { it.events.isEmpty() }
}
