package net.squanchy.search

import io.reactivex.Observable
import io.reactivex.functions.Function3
import net.squanchy.schedule.domain.view.Event
import net.squanchy.search.algolia.AlgoliaSearchEngine
import net.squanchy.search.algolia.model.AlgoliaSearchResult
import net.squanchy.search.algolia.model.AlgoliaSearchResult.QueryNotLongEnough
import net.squanchy.search.algolia.model.AlgoliaSearchResult.ErrorSearching
import net.squanchy.search.algolia.model.AlgoliaSearchResult.Matches
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

    private fun combiner(): Function3<List<Event>, List<Speaker>, AlgoliaSearchResult, SearchResults> {
        return Function3 { events, speakers, result ->
            when (result) {
                is QueryNotLongEnough -> SearchResults(events, speakers)
                //TODO handle the error
                is ErrorSearching -> SearchResults(events, speakers)
                is Matches -> SearchResults(
                    events.filter { result.eventIds.contains(it.id) },
                    speakers.filter { result.speakerIds.contains(it.id) }
                )
            }
        }
    }

    fun speakers(): Observable<List<Speaker>> {
        return speakerRepository.speakers()
            .map { it.sortedBy(Speaker::name) }
    }
}
