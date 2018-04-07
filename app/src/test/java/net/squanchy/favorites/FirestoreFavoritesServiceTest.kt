package net.squanchy.favorites

import arrow.core.Option
import io.reactivex.Observable
import net.squanchy.schedule.ScheduleService
import net.squanchy.schedule.domain.view.aDay
import net.squanchy.schedule.domain.view.aSchedule
import net.squanchy.schedule.domain.view.aSchedulePage
import net.squanchy.schedule.domain.view.anEvent
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.repository.User
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class FirestoreFavoritesServiceTest {

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var authService: FirebaseAuthService

    @Mock
    private lateinit var scheduleService: ScheduleService

    private lateinit var favoritesService: FirestoreFavoritesService

    @Before
    fun setUp() {
        favoritesService = FirestoreFavoritesService(authService, scheduleService)
    }

    @Test
    fun `should return an empty list when there are no favorite events`() {
        val schedule = aSchedule(pages = emptyList())
        `when`(scheduleService.schedule(onlyFavorites = true)).thenReturn(Observable.just(schedule))

        favoritesService.favorites()
            .test()
            .assertValue(emptyList())
    }

    @Test
    fun `should return a header item for each day followed by the days' events`() {
        val schedule = aSchedule(
            pages = listOf(
                aSchedulePage(
                    date = aDay().date,
                    events = listOf(anEvent(id = "day 1 event 1"), anEvent(id = "day 1 event 2"))
                ),
                aSchedulePage(
                    date = aDay().date.plusDays(1),
                    events = listOf(anEvent(id = "day 2 event 1"))
                )
            )
        )
        `when`(scheduleService.schedule(onlyFavorites = true)).thenReturn(Observable.just(schedule))

        favoritesService.favorites()
            .test()
            .assertValue(
                listOf(
                    aFavoriteHeaderListItem(aDay().date),
                    aFavoriteItemListItem(anEvent(id = "day 1 event 1")),
                    aFavoriteItemListItem(anEvent(id = "day 1 event 2")),
                    aFavoriteHeaderListItem(aDay().date.plusDays(1)),
                    aFavoriteItemListItem(anEvent(id = "day 2 event 1"))
                )
            )
    }

    @Test
    fun `should not add a header item for a page with no favourites`() {
        val schedule = aSchedule(
            pages = listOf(
                aSchedulePage(events = emptyList())
            )
        )
        `when`(scheduleService.schedule(onlyFavorites = true)).thenReturn(Observable.just(schedule))

        favoritesService.favorites()
            .test()
            .assertValue(emptyList())
    }

    @Test
    fun `should return false when the user is not signed in`() {
        `when`(authService.currentUser()).thenReturn(Observable.just(Option.absent()))

        favoritesService.currentUserIsSignedIn()
            .test()
            .assertValue(false)
    }

    @Test
    fun `should return true when the user is signed in`() {
        `when`(authService.currentUser()).thenReturn(Observable.just(Option(mock(User::class.java))))

        favoritesService.currentUserIsSignedIn()
            .test()
            .assertValue(true)
    }
}
