package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import net.squanchy.schedule.domain.view.Day
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.service.DaysRepository
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.repository.EventRepository

class ScheduleService internal constructor(
        private val authService: FirebaseAuthService,
        private val eventRepository: EventRepository,
        private val daysRepository: DaysRepository
) {

    fun schedule(onlyFavorites: Boolean): Observable<Schedule> {
        return authService.ifUserSignedInThenObservableFrom { userId ->
            val daysObservable = daysRepository.days()

            val eventsObservable = eventRepository.events(userId)
                .map { events -> if (onlyFavorites) events.filter { it.favorited } else events }
                .map { it.groupBy { it.dayId } }

            eventsObservable
                .withLatestFrom(daysObservable, combineEventsById())
                .subscribeOn(Schedulers.io())
        }
    }

    private fun combineEventsById(): BiFunction<Map<String, List<Event>>, List<Day>, Schedule> {
        return BiFunction { eventsMap, days ->
            val pages = eventsMap.keys
                .mapNotNull(findDayById(days))
                .map(toSchedulePage(eventsMap))
                .sortedBy { it.date }

            Schedule.create(pages)
        }
    }

    private fun findDayById(days: List<Day>): (String) -> Day? {
        return { dayId -> days.find { (id) -> id == dayId } }
    }

    private fun toSchedulePage(eventsMap: Map<String, List<Event>>): (Day) -> SchedulePage {
        return { (id, date) ->
            val events = (eventsMap[id] ?: emptyList()).sortedBy { it.startTime }
            SchedulePage(id, date, events)
        }
    }

    fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }
}
