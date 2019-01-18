package net.squanchy.notification

import io.reactivex.Single
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.anEvent
import net.squanchy.support.system.TestCurrentTime
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.threeten.bp.Duration
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class UpcomingEventsServiceTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var service: NotificationService

    private val currentTime = TestCurrentTime(NOW)

    private lateinit var upcomingEventsService: UpcomingEventsService

    @Before
    fun setUp() {
        upcomingEventsService = UpcomingEventsService(
            service,
            currentTime,
            UPCOMING_EVENT_THRESHOLD
        )
    }

    @Test
    fun upcomingEventsEmitsOnlyEventsStartingAfterNowInsideInterval() {
        givenServiceWillReturn(
            BEFORE_NOW,
            BEFORE_NOW_OTHER_TIMEZONE,
            AFTER_NOW_INSIDE_INTERVAL,
            AFTER_NOW_INSIDE_INTERVAL_OTHER_TIMEZONE,
            AFTER_NOW_OUTSIDE_INTERVAL,
            AFTER_NOW_OUTSIDE_INTERVAL_OTHER_TIMEZONE
        )

        upcomingEventsService.upcomingEvents().test()
            .assertValue(
                listOf(
                    AFTER_NOW_INSIDE_INTERVAL,
                    AFTER_NOW_INSIDE_INTERVAL_OTHER_TIMEZONE
                )
            )
    }

    @Test
    fun nextEventsEmitsOnlyEventsStartingAfterNowOutsideInterval() {
        givenServiceWillReturn(
            BEFORE_NOW,
            BEFORE_NOW_OTHER_TIMEZONE,
            AFTER_NOW_INSIDE_INTERVAL,
            AFTER_NOW_INSIDE_INTERVAL_OTHER_TIMEZONE,
            AFTER_NOW_OUTSIDE_INTERVAL,
            AFTER_NOW_OUTSIDE_INTERVAL_OTHER_TIMEZONE
        )

        upcomingEventsService.nextEvents().test()
            .assertValue(
                listOf(
                    AFTER_NOW_OUTSIDE_INTERVAL,
                    AFTER_NOW_OUTSIDE_INTERVAL_OTHER_TIMEZONE
                )
            )
    }

    private fun givenServiceWillReturn(vararg events: Event) {
        Mockito.`when`(service.sortedFavourites())
            .thenReturn(Single.just(events.asList()))
    }

    companion object {
        private val USER_ZONE_ID = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(0))
        private val ONE_HOUR_EARLIER_ZONE_ID = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1))

        private val NOW = ZonedDateTime.of(
            2018,
            12,
            20,
            11,
            25,
            0,
            0,
            USER_ZONE_ID
        )

        val UPCOMING_EVENT_THRESHOLD: Duration = Duration.ofMinutes(30)

        val BEFORE_NOW = anEvent(
            title = "Before now",
            startTime = NOW.minusHours(1).toLocalDateTime(),
            endTime = NOW.plusHours(4).toLocalDateTime(),
            timeZone = USER_ZONE_ID
        )

        val BEFORE_NOW_OTHER_TIMEZONE = anEvent(
            title = "Before now other timezone",
            startTime = NOW.toLocalDateTime().plusMinutes(30),
            endTime = NOW.plusHours(4).toLocalDateTime(),
            timeZone = ONE_HOUR_EARLIER_ZONE_ID
        )

        val AFTER_NOW_INSIDE_INTERVAL = anEvent(
            title = "After now inside interval",
            startTime = NOW.plus(UPCOMING_EVENT_THRESHOLD.dividedBy(2)).toLocalDateTime(),
            endTime = NOW.plusHours(4).toLocalDateTime(),
            timeZone = USER_ZONE_ID
        )

        val AFTER_NOW_INSIDE_INTERVAL_OTHER_TIMEZONE = anEvent(
            title = "After now inside interval other timezone",
            startTime = NOW.plusHours(1).plus(UPCOMING_EVENT_THRESHOLD.dividedBy(2)).toLocalDateTime(),
            endTime = NOW.plusHours(4).toLocalDateTime(),
            timeZone = ONE_HOUR_EARLIER_ZONE_ID
        )

        val AFTER_NOW_OUTSIDE_INTERVAL = anEvent(
            title = "After now outside interval",
            startTime = NOW.plus(UPCOMING_EVENT_THRESHOLD.multipliedBy(2)).toLocalDateTime(),
            endTime = NOW.plusHours(4).toLocalDateTime(),
            timeZone = USER_ZONE_ID
        )

        val AFTER_NOW_OUTSIDE_INTERVAL_OTHER_TIMEZONE = anEvent(
            title = "After now outside interval other timezone",
            startTime = NOW.plusHours(1).plus(UPCOMING_EVENT_THRESHOLD.multipliedBy(2)).toLocalDateTime(),
            endTime = NOW.plusHours(4).toLocalDateTime(),
            timeZone = ONE_HOUR_EARLIER_ZONE_ID
        )
    }
}
