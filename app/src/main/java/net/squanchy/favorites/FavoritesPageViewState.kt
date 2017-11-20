package net.squanchy.favorites

import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule

/**
 * Represents all the states that the favorites page view can be in.
 * It can only be in one state at at time.
 */
internal sealed class FavoritesPageViewState {

    object Idle : FavoritesPageViewState()

    object ShowSearch : FavoritesPageViewState()

    object ShowSettings : FavoritesPageViewState()

    object RequestSignIn : FavoritesPageViewState()

    object PromptToFavorite : FavoritesPageViewState()

    object PromptToSign : FavoritesPageViewState()

    data class ShowEventDetails(val event: Event) : FavoritesPageViewState()

    data class ShowSchedule(val schedule: Schedule) : FavoritesPageViewState()

}