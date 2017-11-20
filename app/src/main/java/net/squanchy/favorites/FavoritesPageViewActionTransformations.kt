package net.squanchy.favorites

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import net.squanchy.favorites.FavoritesPageViewActions.*
import net.squanchy.favorites.FavoritesPageViewResult.*
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule


/**
 * Converts each action that the user can perform on the FavoritesPageView into an intermediate result that is used by the presenter to
 * decide the state that the view should display.
 */

internal fun loadScheduleTransformer(loadScheduleAction: Observable<LoadSchedule>): Observable<LoadScheduleResult> {
    return loadScheduleAction.flatMap { action ->
        val service = action.service
        Observable.combineLatest(
                service.schedule(action.onlyFavorites), service.currentUserIsSignedIn(),
                BiFunction<Schedule, Boolean, LoadScheduleResult> { schedule, signedIn ->
                    LoadScheduleResult(schedule, signedIn)
                }
        )
    }
}

internal fun eventClickTransformer(eventClickedAction: Observable<EventClicked>): Observable<EventClickResult> {
    return eventClickedAction.map { eventClicked -> EventClickResult(eventClicked.event) }
}

internal fun searchClickTranformer(searchClickedAction: Observable<SearchClicked>): Observable<SearchClickResult> {
    return searchClickedAction.map { SearchClickResult }
}

internal fun settingsClickTransformer(settingsClickedAction: Observable<SettingsClicked>): Observable<SettingsClickResult> {
    return settingsClickedAction.map { SettingsClickResult }
}

internal fun requestSignInTransformer(requestSignInAction: Observable<RequestSignIn>): Observable<RequestSignInResult> {
    return requestSignInAction.map { RequestSignInResult }
}

/**
 * Represents the intermediate result of each action.
 */
internal sealed class FavoritesPageViewResult {

    data class LoadScheduleResult(val schedule: Schedule, val signedIn: Boolean) : FavoritesPageViewResult()

    data class EventClickResult(val event: Event) : FavoritesPageViewResult()

    object SearchClickResult : FavoritesPageViewResult()

    object SettingsClickResult : FavoritesPageViewResult()

    object RequestSignInResult : FavoritesPageViewResult()

}