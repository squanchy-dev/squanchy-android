package net.squanchy.search

import io.reactivex.Observable
import io.reactivex.functions.Function3
import net.squanchy.schedule.domain.view.Event
import net.squanchy.search.algolia.AlgoliaSearchEngine
import net.squanchy.search.algolia.model.AlgoliaSearchResult
import net.squanchy.search.algolia.model.AlgoliaSearchResult.QueryNotLongEnough
import net.squanchy.search.algolia.model.AlgoliaSearchResult.ErrorSearching
import net.squanchy.search.algolia.model.AlgoliaSearchResult.Matches
import net.squanchy.search.SearchListElement.SpeakerHeader
import net.squanchy.search.SearchListElement.EventHeader
import net.squanchy.search.SearchListElement.SpeakerElement
import net.squanchy.search.SearchListElement.AlgoliaLogo
import net.squanchy.search.SearchListElement.EventElement
import net.squanchy.service.repository.AuthService
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.speaker.domain.view.Speaker

class SearchService(
    private val eventRepository: EventRepository,
    private val speakerRepository: SpeakerRepository,
    private val authService: AuthService,
    private val algoliaSearchEngine: AlgoliaSearchEngine
) {

    fun find(query: String): Observable<SearchResult> {
        return authService.ifUserSignedInThenObservableFrom { userId ->
            Observable.combineLatest(
                eventRepository.events(userId),
                speakerRepository.speakers(),
                algoliaSearchEngine.query(query),
                combineWithSearchResult()
            )
        }
    }

    private fun combineWithSearchResult(): Function3<List<Event>, List<Speaker>, AlgoliaSearchResult, SearchResult> {
        return Function3 { events, speakers, result ->
            when (result) {
                is QueryNotLongEnough -> SearchResult.Success(createResultForQueryNotLongEnough(speakers))
                is ErrorSearching -> SearchResult.Error
                is Matches -> {
                    val filteredEvents = events.filter { result.eventIds.contains(it.id) }
                    val filteredSpeakers = speakers.filter { result.speakerIds.contains(it.id) }
                    SearchResult.Success(createResultForSuccessfulSearch(filteredEvents, filteredSpeakers))
                }
            }
        }
    }

    private fun createResultForQueryNotLongEnough(speakers: List<Speaker>): List<SearchListElement> {
        return listOf(SpeakerHeader) + speakers.map(::SpeakerElement)
    }

    private fun createResultForSuccessfulSearch(events: List<Event>, speakers: List<Speaker>): List<SearchListElement> {
        if (speakers.isEmpty() && events.isEmpty()) {
            return emptyList()
        }
        val list = ArrayList<SearchListElement>(events.size + speakers.size)
        if (events.isNotEmpty()) {
            list.add(EventHeader)
            list.addAll(events.map(::EventElement))
        }
        if (speakers.isNotEmpty()) {
            list.add(SpeakerHeader)
            list.addAll(speakers.map(::SpeakerElement))
        }
        list.add(AlgoliaLogo)
        return list
    }

    fun speakers(): Observable<List<Speaker>> {
        return speakerRepository.speakers()
            .map { it.sortedBy(Speaker::name) }
    }
}
