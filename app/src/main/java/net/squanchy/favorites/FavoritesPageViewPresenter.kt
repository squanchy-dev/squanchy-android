package net.squanchy.favorites

import io.reactivex.Observable
import net.squanchy.favorites.FavoritesPageViewActions.*
import net.squanchy.schedule.domain.view.Schedule

internal fun favoritesPageViewPresenter(actions: Observable<FavoritesPageViewActions>): Observable<FavoritesPageViewState> {

    return actions.publish {

        val loadScheduleAction = actions.ofType(LoadSchedule::class.java).compose(::loadScheduleTransformer)

        val eventClicked = actions.ofType(EventClicked::class.java).compose(::eventClickTransformer)

        val searchClicked = actions.ofType(SearchClicked::class.java).compose(::searchClickTranformer)

        val settingsClicked = actions.ofType(SettingsClicked::class.java).compose(::settingsClickTransformer)

        val requestSignIn = actions.ofType(RequestSignIn::class.java).compose(::requestSignInTransformer)

        val resultList = listOf(loadScheduleAction, eventClicked, searchClicked, settingsClicked, requestSignIn)

        val mergedActionResult: Observable<FavoritesPageViewResult> = Observable.merge(resultList)

        val initialViewState: FavoritesPageViewState = FavoritesPageViewState.Idle

        val viewState = mergedActionResult.scan(initialViewState, ::viewStateReducer)

        return@publish viewState
    }
}

/**
 * previousState is not used here since all the states are mutually exclusive. Just keeping it here in case it's needed in future.
 */
private fun viewStateReducer(previousState: FavoritesPageViewState, result: FavoritesPageViewResult): FavoritesPageViewState {

    when (result) {

        is FavoritesPageViewResult.LoadScheduleResult -> return handleLoadSchedule(result)

        is FavoritesPageViewResult.EventClickResult -> return handleEventClick(result)

        is FavoritesPageViewResult.SearchClickResult -> return handleSearchClick(result)

        is FavoritesPageViewResult.SettingsClickResult -> return handleSettingsClick(result)

        is FavoritesPageViewResult.RequestSignInResult -> return handleRequestSignIn(result)

        else -> throw IllegalStateException("Cannot handle state $previousState for result $result")
    }

}

private fun handleLoadSchedule(result: FavoritesPageViewResult.LoadScheduleResult): FavoritesPageViewState {

    fun hasFavorites(schedule: Schedule) = !schedule.isEmpty

    if (hasFavorites(result.schedule)) return FavoritesPageViewState.ShowSchedule(result.schedule)

    return if (result.signedIn) FavoritesPageViewState.PromptToFavorite else FavoritesPageViewState.PromptToSign
}


private fun handleEventClick(result: FavoritesPageViewResult.EventClickResult) = FavoritesPageViewState.ShowEventDetails(result.event)

private fun handleSearchClick(result: FavoritesPageViewResult.SearchClickResult) = FavoritesPageViewState.ShowSearch

private fun handleSettingsClick(result: FavoritesPageViewResult.SettingsClickResult) = FavoritesPageViewState.ShowSettings

private fun handleRequestSignIn(result: FavoritesPageViewResult.RequestSignInResult) = FavoritesPageViewState.RequestSignIn