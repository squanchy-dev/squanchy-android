package net.squanchy.favorites

import io.reactivex.Observable
import net.squanchy.favorites.view.FavoritesItem
import net.squanchy.schedule.ScheduleService
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.service.repository.AuthService
import net.squanchy.support.lang.or

interface FavoritesService {

    fun favorites(): Observable<List<FavoritesItem>>

    fun currentUserIsSignedIn(): Observable<Boolean>
}

internal class FirestoreFavoritesService(
    private val authService: AuthService,
    private val scheduleService: ScheduleService
) : FavoritesService {

    override fun favorites(): Observable<List<FavoritesItem>> {
        return scheduleService.schedule()
            .map { schedule -> schedule.pages }
            .flatMap { pages ->
                val flattenedItems = pages.map { page -> page.asFavouritesDayViewModel() }
                    .flatten()

                return@flatMap Observable.just(flattenedItems)
            }
    }

    private fun SchedulePage.asFavouritesDayViewModel(): List<FavoritesItem> {
        val eventsAsFavoriteItems = this.events
            .filter { it.favorited }
            .map { it.toFavoriteItem() }

        if (eventsAsFavoriteItems.isNotEmpty()) {
            return listOf(FavoritesItem.Header(date)) + eventsAsFavoriteItems
        } else {
            return emptyList()
        }
    }

    override fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }

    private fun Event.toFavoriteItem(): FavoritesItem.Favorite = FavoritesItem.Favorite(this)
}
