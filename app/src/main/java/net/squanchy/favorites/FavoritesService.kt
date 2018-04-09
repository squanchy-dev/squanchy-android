package net.squanchy.favorites

import io.reactivex.Observable
import net.squanchy.favorites.view.FavoritesItem
import net.squanchy.schedule.ScheduleService
import net.squanchy.schedule.domain.view.Event
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
        return scheduleService.schedule(onlyFavorites = true)
            .map { schedule -> schedule.pages }
            .flatMap { pages ->
                val flattenedItems = pages.map { page ->
                    val eventsAsFavoriteItems = page.events.map { it.toFavoriteItem() }

                    if (eventsAsFavoriteItems.isNotEmpty()) {
                        return@map listOf(FavoritesItem.Header(page.date)) + eventsAsFavoriteItems
                    } else {
                        return@map emptyList<FavoritesItem>()
                    }
                }.flatten()

                return@flatMap Observable.just(flattenedItems)
            }
    }

    override fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }

    private fun Event.toFavoriteItem(): FavoritesItem.Favorite = FavoritesItem.Favorite(this)
}
