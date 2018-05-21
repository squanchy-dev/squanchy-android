package net.squanchy.schedule.domain.view

import org.joda.time.DateTimeZone

data class Schedule(val pages: List<SchedulePage>, val timeZone: DateTimeZone) {

    val isEmpty: Boolean
        get() = pages.all { it.events.isEmpty() }
}
