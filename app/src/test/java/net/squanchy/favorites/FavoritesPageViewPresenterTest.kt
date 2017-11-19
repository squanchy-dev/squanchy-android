package net.squanchy.favorites

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import net.squanchy.schedule.ScheduleService
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.support.lang.Optional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FavoritesPageViewPresenterTest {

    private val testScheduleData = Schedule(listOf(SchedulePage("abc", LocalDateTime.now(), emptyList()),
            SchedulePage("dsds", LocalDateTime.now(), emptyList())))

    @Mock
    private lateinit var service: ScheduleService

    private val actions = PublishSubject.create<FavoritesPageViewActions>()

    private lateinit var testObserver: TestObserver<FavoritesPageViewState>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        testObserver = TestObserver<FavoritesPageViewState>()
    }

    @After
    fun cleanUp() {
        testObserver.dispose()
    }

    @Test
    fun `initial view state should be idle`() {
        //when
        val viewState = favoritesPageViewPresenter(actions)

        //then
        viewState.subscribe(testObserver)

        testObserver.assertValueCount(1)
        testObserver.assertValue(FavoritesPageViewState.Idle)
        testObserver.assertNotComplete()

    }

    @Test
    fun `display schedule when schedule has favorites and user is signed in`() {
        //given
        Mockito.`when`(service.schedule(true)).thenReturn(Observable.just(testScheduleData))
        Mockito.`when`(service.currentUserIsSignedIn()).thenReturn(Observable.just(true))

        //when
        val viewState = favoritesPageViewPresenter(actions)

        //then
        viewState.subscribe(testObserver)

        actions.onNext(FavoritesPageViewActions.LoadSchedule(true, service))

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(0, FavoritesPageViewState.Idle)
        testObserver.assertValueAt(1, FavoritesPageViewState.ShowSchedule(testScheduleData))
    }

    @Test
    fun `prompt to favorites when user is signed in and schedule is empty`() {
        //given
        Mockito.`when`(service.schedule(true)).thenReturn(Observable.just(Schedule(emptyList())))
        Mockito.`when`(service.currentUserIsSignedIn()).thenReturn(Observable.just(true))

        //when
        val viewState = favoritesPageViewPresenter(actions)

        //then
        viewState.subscribe(testObserver)

        actions.onNext(FavoritesPageViewActions.LoadSchedule(true, service))

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(0, FavoritesPageViewState.Idle)
        testObserver.assertValueAt(1, FavoritesPageViewState.PromptToFavorite)
    }

    @Test
    fun `prompt to sign in when user is not signed in and schedule is empty`() {
        //given
        Mockito.`when`(service.schedule(true)).thenReturn(Observable.just(Schedule(emptyList())))
        Mockito.`when`(service.currentUserIsSignedIn()).thenReturn(Observable.just(false))

        //when
        val viewState = favoritesPageViewPresenter(actions)

        //then
        viewState.subscribe(testObserver)

        actions.onNext(FavoritesPageViewActions.LoadSchedule(true, service))

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(0, FavoritesPageViewState.Idle)
        testObserver.assertValueAt(1, FavoritesPageViewState.PromptToSign)
    }

    @Test
    fun `display schedule when schedule has favorites and user is not signed in`() {
        //given
        Mockito.`when`(service.schedule(true)).thenReturn(Observable.just(testScheduleData))
        Mockito.`when`(service.currentUserIsSignedIn()).thenReturn(Observable.just(false))

        //when
        val viewState = favoritesPageViewPresenter(actions)

        //then
        viewState.subscribe(testObserver)

        actions.onNext(FavoritesPageViewActions.LoadSchedule(true, service))

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(0, FavoritesPageViewState.Idle)
        testObserver.assertValueAt(1, FavoritesPageViewState.ShowSchedule(testScheduleData))
    }

    @Test
    fun `display event details when prompted`() {
        //given
        val event = Event("nvn", 123L, LocalDateTime.now(), LocalDateTime.now(), "testEvent", Optional.absent(), Optional.absent(),
                emptyList(), Optional.absent(), "today", Event.Type.COFFEE_BREAK, false, Optional.absent(), DateTimeZone.UTC)

        //when
        val viewState = favoritesPageViewPresenter(actions)

        //then
        viewState.subscribe(testObserver)

        actions.onNext(FavoritesPageViewActions.EventClicked(event))

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(0, FavoritesPageViewState.Idle)
        testObserver.assertValueAt(1, FavoritesPageViewState.ShowEventDetails(event))
    }

    @Test
    fun `display schedule and then event details`() {
        //given
        val event = Event("nvn", 123L, LocalDateTime.now(), LocalDateTime.now(), "testEvent", Optional.absent(), Optional.absent(),
                emptyList(), Optional.absent(), "today", Event.Type.COFFEE_BREAK, false, Optional.absent(), DateTimeZone.UTC)

        Mockito.`when`(service.schedule(true)).thenReturn(Observable.just(testScheduleData))
        Mockito.`when`(service.currentUserIsSignedIn()).thenReturn(Observable.just(false))

        //when
        val viewState = favoritesPageViewPresenter(actions)

        //then
        viewState.subscribe(testObserver)

        actions.onNext(FavoritesPageViewActions.LoadSchedule(true, service))
        actions.onNext(FavoritesPageViewActions.EventClicked(event))

        testObserver.assertValueCount(3)
        testObserver.assertValueAt(0, FavoritesPageViewState.Idle)
        testObserver.assertValueAt(1, FavoritesPageViewState.ShowSchedule(testScheduleData))
        testObserver.assertValueAt(2, FavoritesPageViewState.ShowEventDetails(event))

    }

    @Test
    fun `display search view when clicked`() {
        //when
        val viewState = favoritesPageViewPresenter(actions)

        //then
        viewState.subscribe(testObserver)

        actions.onNext(FavoritesPageViewActions.SearchClicked)

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1, FavoritesPageViewState.ShowSearch)
        testObserver.assertNotComplete()
    }

    @Test
    fun `display settings view when clicked`() {
        //when
        val viewState = favoritesPageViewPresenter(actions)

        //then
        viewState.subscribe(testObserver)

        actions.onNext(FavoritesPageViewActions.SettingsClicked)

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1, FavoritesPageViewState.ShowSettings)
        testObserver.assertNotComplete()
    }

    @Test
    fun `request sign in when prompted`() {
        //when
        val viewState = favoritesPageViewPresenter(actions)

        //then
        viewState.subscribe(testObserver)

        actions.onNext(FavoritesPageViewActions.RequestSignIn)

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1, FavoritesPageViewState.RequestSignIn)
        testObserver.assertNotComplete()
    }
}