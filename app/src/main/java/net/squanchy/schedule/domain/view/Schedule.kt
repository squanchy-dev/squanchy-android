package net.squanchy.schedule.domain.view

data class Schedule(val pages: List<SchedulePage>) {

    val isEmpty: Boolean
        get() = pages.isEmpty()

    companion object {

        fun create(pages: List<SchedulePage>): Schedule {
            return Schedule(pages)
        }
    }
}
