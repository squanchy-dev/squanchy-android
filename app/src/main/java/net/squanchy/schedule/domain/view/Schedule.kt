package net.squanchy.schedule.domain.view

import net.squanchy.support.system.CurrentTime
import org.joda.time.DateTimeZone

private const val NOT_FOUND_INDEX = -1
private const val FIRST_PAGE_INDEX = 0

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
                    NOT_FOUND_INDEX -> FIRST_PAGE_INDEX
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
