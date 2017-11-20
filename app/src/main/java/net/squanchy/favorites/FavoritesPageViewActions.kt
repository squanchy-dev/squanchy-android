package net.squanchy.favorites

import net.squanchy.schedule.ScheduleService
import net.squanchy.schedule.domain.view.Event

/**
* Represents actions that the user can perform on the favorites page view.
**/

internal sealed class FavoritesPageViewActions {

    data class LoadSchedule(val onlyFavorites: Boolean, val service: ScheduleService) : FavoritesPageViewActions()

    data class EventClicked(val event: Event) : FavoritesPageViewActions()

    object SearchClicked : FavoritesPageViewActions()

    object SettingsClicked : FavoritesPageViewActions()

    object RequestSignIn : FavoritesPageViewActions()

}