package net.squanchy.favorites

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import net.squanchy.favorites.view.FavoritesEmptyViewState
import net.squanchy.favorites.view.FavoritesSignedInEmptyLayout
import net.squanchy.favorites.view.favoritesSignedInEmptyLayoutPresenter
import org.junit.Test

class FavoritesSignedInEmptyLayoutPresenterTest {

    @Test
    fun `first time, return counter as 1, filled icon`() {
        //given
        val input = Observable.just(FavoritesSignedInEmptyLayout.FavoritesClickEvent(0))

        //when
        val viewState = favoritesSignedInEmptyLayoutPresenter(input)

        val testObserver = TestObserver<FavoritesEmptyViewState>()

        viewState.subscribe(testObserver)

        //then
        val expected = FavoritesEmptyViewState(1, true, false, false)

        testObserver.assertValueCount(1)
        testObserver.assertValue(expected)
    }

    @Test
    fun `when input counter is odd, empty icon`() {

        //given
        val input = Observable.just(FavoritesSignedInEmptyLayout.FavoritesClickEvent(3))

        //when
        val viewState = favoritesSignedInEmptyLayoutPresenter(input)

        val testObserver = TestObserver<FavoritesEmptyViewState>()

        viewState.subscribe(testObserver)

        //then
        val expected = FavoritesEmptyViewState(4, false, false, false)

        testObserver.assertValueCount(1)
        testObserver.assertValue(expected)
    }

    @Test
    fun `when input counter is 4, filled icon, fast learner`() {
        //given
        val input = Observable.just(FavoritesSignedInEmptyLayout.FavoritesClickEvent(4))

        //when
        val viewState = favoritesSignedInEmptyLayoutPresenter(input)

        val testObserver = TestObserver<FavoritesEmptyViewState>()

        viewState.subscribe(testObserver)

        //then
        val expected = FavoritesEmptyViewState(5, true, true, false)

        testObserver.assertValueCount(1)
        testObserver.assertValue(expected)
    }

    @Test
    fun `when input counter is 14, filled icon, perseverant`() {
        //given
        val input = Observable.just(FavoritesSignedInEmptyLayout.FavoritesClickEvent(14))

        //when
        val viewState = favoritesSignedInEmptyLayoutPresenter(input)

        val testObserver = TestObserver<FavoritesEmptyViewState>()

        viewState.subscribe(testObserver)

        //then
        val expected = FavoritesEmptyViewState(15, true, false, true)

        testObserver.assertValueCount(1)
        testObserver.assertValue(expected)
    }
}