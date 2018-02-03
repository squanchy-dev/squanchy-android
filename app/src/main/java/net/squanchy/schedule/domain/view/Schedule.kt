package net.squanchy.schedule.domain.view

import net.squanchy.support.system.CurrentTime
import org.joda.time.DateTimeZone

data class Schedule(val pages: List<SchedulePage>, val timezone: DateTimeZone) {

    val isEmpty: Boolean
        get() = pages.isEmpty()

    companion object {

        fun create(pages: List<SchedulePage>, timezone: DateTimeZone) = Schedule(pages, timezone)
    }

    fun findTodayIndexOrDefault(currentTime: CurrentTime) =
        pages
            .indexOfFirst { page ->
                val now = currentTime.currentDateTime().toDateTime(timezone)
                page.date.toLocalDate().isEqual(now.toLocalDate())
            }
            .let {
                when (it) {
                    -1 -> 0 // default to the first page
                    else -> it
                }
            }

    fun findNextEventForPage(page: SchedulePage, currentTime: CurrentTime) =
        page
            .events
            .firstOrNull { event ->
                val startDateTime = event.startTime.toDateTime().withZone(event.timeZone)
                val currentDateTime = currentTime.currentDateTime().toDateTime().withZone(event.timeZone)
                startDateTime.isAfter(currentDateTime)
            }
}
