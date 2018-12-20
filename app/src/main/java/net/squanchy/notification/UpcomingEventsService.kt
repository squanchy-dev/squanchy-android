package net.squanchy.notification

import io.reactivex.Single
import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.system.CurrentTime
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime

class UpcomingEventsService(
    private val service: NotificationService,
    private val currentTime: CurrentTime,
    private val upcomingEventThreshold: Duration
) {

    fun upcomingEvents(): Single<List<Event>> {
        val now = currentTime.currentDateTime()
        val notificationEndTime = now.plus(upcomingEventThreshold)

        return service.sortedFavourites()
            .map { events -> events.filter { it.zonedStartTime.isAfter(now) } }
            .map { events -> events.filter { it.zonedStartTime.isBeforeOrEqualTo(notificationEndTime) } }
    }

    private fun ZonedDateTime.isBeforeOrEqualTo(other: ZonedDateTime) =
        isBefore(other) || isEqual(other)

    fun nextEvents(): Single<List<Event>> {
        val now = currentTime.currentDateTime()
        val notificationEndTime = now.plus(upcomingEventThreshold)

        return service.sortedFavourites()
            .map { events -> events.filter { it.zonedStartTime.isAfter(notificationEndTime) } }
    }
}
