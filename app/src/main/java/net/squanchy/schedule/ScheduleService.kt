package net.squanchy.schedule

import io.reactivex.Observable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firebase.FirebaseDbService
import net.squanchy.service.firebase.model.FirebaseDay
import net.squanchy.service.firebase.model.FirebaseDays
import net.squanchy.service.firebase.model.FirebaseVenue
import net.squanchy.service.repository.EventRepository
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

private val dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")

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
                .withLatestFrom(daysObservable, dbService.venueInfo(), combineEventsById())
                .subscribeOn(Schedulers.io())
        }
    }

    private fun combineEventsById(): Function3<Map<String, List<Event>>, FirebaseDays, FirebaseVenue, Schedule> {
        return Function3 { eventsMap, (days), venue ->
            val pages = eventsMap.keys
                .mapNotNull(findDayById(days!!))
                .map(toSchedulePage(eventsMap))
                .sortedBy { it.date }

            val timezone = DateTimeZone.forID(venue.timezone)
            Schedule.create(pages, timezone)
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
            val parsedDate = LocalDateTime.parse(date!!, dateFormatter)
            val events = (eventsMap[id!!] ?: emptyList()).sortedBy { it.startTime }
            SchedulePage.create(id, parsedDate, events)
        }
    }

    fun currentUserIsSignedIn(): Observable<Boolean> {
        return authService.currentUser()
            .map { optionalUser -> optionalUser.map { user -> !user.isAnonymous }.or(false) }
    }
}
