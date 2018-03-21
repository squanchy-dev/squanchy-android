package net.squanchy.search

import io.reactivex.Observable
import io.reactivex.functions.Function3
import net.squanchy.schedule.domain.view.Event
import net.squanchy.search.algolia.AlgoliaSearchEngine
import net.squanchy.search.algolia.model.AlgoliaMatches
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.speaker.domain.view.Speaker

class SearchService(
    private val eventRepository: EventRepository,
    private val speakerRepository: SpeakerRepository,
    private val authService: FirebaseAuthService,
    private val algoliaSearchEngine: AlgoliaSearchEngine
) {

    fun find(query: String): Observable<SearchResults> {
        return authService.ifUserSignedInThenObservableFrom { userId ->
            Observable.combineLatest(
                eventRepository.events(userId),
                speakerRepository.speakers(),
                algoliaSearchEngine.query(query),
                combiner()
            )
        }
    }

    private fun combiner(): Function3<List<Event>, List<Speaker>, AlgoliaMatches, SearchResults> {
        return Function3 { events, speakers, matchingIds ->
            SearchResults(
                events.filter { matchingIds.eventIds.contains(it.id) },
                speakers.filter { matchingIds.speakerIds.contains(it.id) }
            )
        }
    }

    fun speakers(): Observable<List<Speaker>> {
        return speakerRepository.speakers()
            .map { it.sortedBy { it.name } }
    }
}
