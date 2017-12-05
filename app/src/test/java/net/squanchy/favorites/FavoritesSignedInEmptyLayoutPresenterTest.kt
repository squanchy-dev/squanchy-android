package net.squanchy.favorites

import net.squanchy.favorites.view.FavoritesSignedInEmptyLayoutView
import net.squanchy.favorites.view.presentAchievementMessage
import net.squanchy.favorites.view.presentButtonIcon
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class FavoritesSignedInEmptyLayoutPresenterTest {

    @Mock
    private lateinit var view: FavoritesSignedInEmptyLayoutView

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `first time, return counter as 1, icon should be filled and no message displayed`() {
        //given
        val counter = 0
        val filledIconProvider = { 2 }

        //when
        presentButtonIcon(counter, view, filledIconProvider, {0})
        presentAchievementMessage(counter, view,  {""}, {""})

        //then
        verify(view).setButtonImage(filledIconProvider())
        verify(view).updateCounter(1)
        verify(view, never()).showAchievement(anyString())
        verifyNoMoreInteractions(view)
    }

    @Test
    fun `when counter is odd, show empty icon`() {
        //given
        val counter = 1
        val emptyIconProvider = {4}

        //when
        presentButtonIcon(counter, view, {0}, emptyIconProvider)
        presentAchievementMessage(counter, view,  {""}, {""})

        //then
        verify(view).setButtonImage(emptyIconProvider())
        verify(view).updateCounter(2)
        verify(view, never()).showAchievement(anyString())
        verifyNoMoreInteractions(view)
    }

    @Test
    fun `when counter is 4, show filled icon, show initial achievement`() {
        //given
        val counter = 4
        val filledIconProvider = {1}
        val initialMessageProvider = {"initial"}

        //when
        presentButtonIcon(counter, view, filledIconProvider, {0})
        presentAchievementMessage(counter, view,  initialMessageProvider, {""})

        //then
        verify(view).setButtonImage(filledIconProvider())
        verify(view).updateCounter(5)
        verify(view).showAchievement(initialMessageProvider())
        verifyNoMoreInteractions(view)
    }

    @Test
    fun `when counter is 14, show filled icon, show perseverant achievement`() {
        //given
        val counter = 14
        val filledIconProvider = {1}
        val perseverantMessageProvider = {"perseverant"}

        //when
        presentButtonIcon(counter, view, filledIconProvider, {0})
        presentAchievementMessage(counter, view,  {""}, perseverantMessageProvider)

        //then
        verify(view).setButtonImage(filledIconProvider())
        verify(view).updateCounter(15)
        verify(view).showAchievement(perseverantMessageProvider())
        verifyNoMoreInteractions(view)
    }
}
