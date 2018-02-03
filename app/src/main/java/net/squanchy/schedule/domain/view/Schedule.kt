package net.squanchy.schedule.domain.view

import org.joda.time.DateTimeZone

data class Schedule(val pages: List<SchedulePage>, val timezone: DateTimeZone) {

    val isEmpty: Boolean
        get() = pages.isEmpty()

    companion object {

        fun create(pages: List<SchedulePage>, timezone: DateTimeZone) = Schedule(pages, timezone)
    }
}
