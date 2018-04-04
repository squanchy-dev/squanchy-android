package net.squanchy.favorites

import io.reactivex.Observable
import net.squanchy.favorites.view.FavoriteListItem
import net.squanchy.schedule.ScheduleService
import net.squanchy.schedule.domain.view.Event
import net.squanchy.service.firebase.FirebaseAuthService

interface FavoritesService {

    fun favorites(): Observable<List<FavoriteListItem>>

    fun currentUserIsSignedIn(): Observable<Boolean>
}

internal class FirestoreFavoritesService(
    private val authService: FirebaseAuthService,
    private val scheduleService: ScheduleService
) : FavoritesService {

    override fun favorites(): Observable<List<FavoriteListItem>> {
        return scheduleService.schedule(onlyFavorites = true)
            .map { schedule -> schedule.pages }
            .flatMap { pages ->
                val flattenedItems = pages.map { page ->
                    val eventsAsFavoriteItems = page.events.map { it.toFavoriteItem() }
                    listOf(FavoriteListItem.Header(page.date)) + eventsAsFavoriteItems
                }.flatten()
                return@flatMap Observable.just(flattenedItems)
            }
    }

    override fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }
}

private fun Event.toFavoriteItem(): FavoriteListItem.Favorite =
    FavoriteListItem.Favorite(this)
