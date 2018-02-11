package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.toEvent
import net.squanchy.support.lang.Checksum
import org.joda.time.LocalDate

interface ScheduleService {
    fun schedule(onlyFavorites: Boolean): Observable<Schedule>
    fun currentUserIsSignedIn(): Observable<Boolean>
}

class FirestoreScheduleService(
    private val authService: FirebaseAuthService,
    private val dbService: FirestoreDbService,
    private val checksum: Checksum
) : ScheduleService {

    override fun schedule(onlyFavorites: Boolean): Observable<Schedule> {
        return Observable.combineLatest(dbService.scheduleView(), dbService.timezone(), combineInAPair())
            .map { pagesAndTimeZone ->

                val (schedulePages, timeZone) = pagesAndTimeZone
                schedulePages.map { schedulePage ->
                    SchedulePage(
                        schedulePage.day.id,
                        LocalDate(schedulePage.day.date),
                        // TODO pass the favourites here instead of null to know which talks are favourites
                        schedulePage.events.map { it.toEvent(checksum, timeZone, null) }
                            .sortedBy { it.startTime }
                            .filterOnlyFavorites(onlyFavorites)
                    )
                }
            }
            .map(::Schedule)
    }

    private fun <T, U> combineInAPair(): BiFunction<List<T>, U, Pair<List<T>, U>> = BiFunction(::Pair)

    private fun List<Event>.filterOnlyFavorites(onlyFavorites: Boolean) = when {
        onlyFavorites -> filter { it.favorited }
        else -> this
    }

    override fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }
}
