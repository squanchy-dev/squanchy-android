package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firebase.FirebaseDbService
import net.squanchy.service.firebase.model.FirebaseDay
import net.squanchy.service.firebase.model.FirebaseDays
import net.squanchy.service.repository.EventRepository
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class ScheduleService internal constructor(
        private val dbService: FirebaseDbService,
        private val authService: FirebaseAuthService,
        private val eventRepository: EventRepository
) {

    fun schedule(onlyFavorites: Boolean): Observable<Schedule> {
        return authService.ifUserSignedInThenObservableFrom { userId ->
            val daysObservable = dbService.days()

            val eventsObservable = eventRepository.events(userId)
                    .map { events -> if (onlyFavorites) events.filter { it.favorited } else events }
                    .map { it.groupBy { it.dayId } }

            eventsObservable
                    .withLatestFrom(daysObservable, combineEventsById())
                    .subscribeOn(Schedulers.io())
        }
    }

    private fun combineEventsById(): BiFunction<Map<String, List<Event>>, FirebaseDays, Schedule> {
        return BiFunction { eventsMap, (days) ->
            val pages = eventsMap.keys
                    .map(findDayById(days!!))
                    .filterNotNull()
                    .map(toSchedulePage(eventsMap))
                    .sortedBy { it.date }

            Schedule.create(pages)
        }
    }

    private fun findDayById(days: List<FirebaseDay>): (String) -> FirebaseDay? {
        return { dayId ->
            days.find { (id) ->
                id == dayId
            }
        }
    }

    private fun toSchedulePage(eventsMap: Map<String, List<Event>>): (FirebaseDay) -> SchedulePage {
        return { (id, date) ->
            val parsedDate = LocalDateTime.parse(date!!, DATE_FORMATTER)
            val events = (eventsMap[id!!] ?: emptyList()).sortedBy { it.startTime }
            SchedulePage.create(id, parsedDate, events)
        }
    }

    fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
                .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }

    companion object {

        private val DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd")
    }
}
