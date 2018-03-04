package net.squanchy.eventdetails

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import net.squanchy.schedule.domain.view.Event
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.repository.EventRepository
import net.squanchy.support.lang.Optional

internal class EventDetailsService(
        private val eventRepository: EventRepository,
        private val authService: FirebaseAuthService
) {

    fun event(eventId: String): Observable<Event> {
        return authService.ifUserSignedInThenObservableFrom { userId -> eventRepository.event(eventId, userId) }
    }

    @SuppressLint("CheckResult") // False positive, to remove in 3.1.0-beta5
    fun toggleFavorite(event: Event): Single<FavoriteResult> {
        return currentUser()
            .flatMap { optionalUser ->
                optionalUser
                    .map {
                        if (it.isAnonymous) {
                            Single.just(FavoriteResult.MUST_AUTHENTICATE)
                        } else {
                            toggleFavoriteOn(event).andThen(Single.just(FavoriteResult.SUCCESS))
                        }
                    }
                    .or(Single.just(FavoriteResult.MUST_AUTHENTICATE))
            }
    }

    private fun toggleFavoriteOn(event: Event): Completable {
        return if (event.favorited) {
            removeFavorite(event.id)
        } else {
            favorite(event.id)
        }
    }

    private fun removeFavorite(eventId: String): Completable {
        return authService.ifUserSignedInThenCompletableFrom { userId -> eventRepository.removeFavorite(eventId, userId) }
    }

    private fun favorite(eventId: String): Completable {
        return authService.ifUserSignedInThenCompletableFrom { userId -> eventRepository.addFavorite(eventId, userId) }
    }

    private fun currentUser(): Single<Optional<FirebaseUser>> = authService.currentUser().firstOrError()

    internal enum class FavoriteResult {
        SUCCESS,
        MUST_AUTHENTICATE
    }
}
