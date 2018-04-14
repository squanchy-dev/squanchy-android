package net.squanchy.search

import io.reactivex.Observable
import net.squanchy.FakeAuthService
import net.squanchy.schedule.domain.view.anEvent
import net.squanchy.search.SearchListElement.EventElement
import net.squanchy.search.SearchListElement.SpeakerElement
import net.squanchy.search.algolia.AlgoliaSearchEngine
import net.squanchy.search.algolia.model.AlgoliaSearchResult
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.speaker.domain.view.aSpeaker
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class SearchServiceTest {

    @Rule
    @JvmField
    var rule: MockitoRule = MockitoJUnit.rule()

    private lateinit var searchService: SearchService

    @Mock
    private lateinit var eventRepository: EventRepository

    @Mock
    private lateinit var speakerRepository: SpeakerRepository

    @Mock
    private lateinit var algoliaSearchEngine: AlgoliaSearchEngine

    @Before
    fun setup() {
        searchService = SearchService(eventRepository, speakerRepository, FakeAuthService(UID), algoliaSearchEngine)
    }

    @Test
    fun `should show only the speakers when the query is not long enough`() {
        val eventList = listOf(anEvent(), anEvent(id = "qwer"))
        val speakerList = listOf(aSpeaker(), aSpeaker(id = "qwer"))
        `when`(algoliaSearchEngine.query(QUERY)).thenReturn(Observable.just(AlgoliaSearchResult.QueryNotLongEnough))
        `when`(eventRepository.events(UID)).thenReturn(Observable.just(eventList))
        `when`(speakerRepository.speakers()).thenReturn(Observable.just(speakerList))
        searchService.find(QUERY)
            .test()
            .assertValue(
                SearchResult.Success(
                    mutableListOf<SearchListElement>().apply {
                        add(SearchListElement.SpeakerHeader)
                        addAll(speakerList.map(::SpeakerElement))
                    }
                )
            )
    }

    @Test
    fun `should filter the results when the query is valid`() {
        val eventList = listOf(anEvent(), anEvent(id = "qwer"))
        val speakerList = listOf(aSpeaker(), aSpeaker(id = "qwer"))
        `when`(algoliaSearchEngine.query(QUERY)).thenReturn(Observable.just(AlgoliaSearchResult.Matches(listOf("qwer"), emptyList())))
        `when`(eventRepository.events(UID)).thenReturn(Observable.just(eventList))
        `when`(speakerRepository.speakers()).thenReturn(Observable.just(speakerList))
        searchService.find(QUERY)
            .test()
            .assertValue(
                SearchResult.Success(
                    mutableListOf<SearchListElement>().apply {
                        add(SearchListElement.EventHeader)
                        addAll(listOf(anEvent(id = "qwer")).map(::EventElement))
                        add(SearchListElement.AlgoliaLogo)
                    }
                )
            )
    }

    @Test
    fun `should receive an error when the algolia search engine fails`() {
        val eventList = listOf(anEvent(), anEvent(id = "qwer"))
        val speakerList = listOf(aSpeaker(), aSpeaker(id = "qwer"))
        `when`(algoliaSearchEngine.query(QUERY)).thenReturn(Observable.just(AlgoliaSearchResult.ErrorSearching))
        `when`(eventRepository.events(UID)).thenReturn(Observable.just(eventList))
        `when`(speakerRepository.speakers()).thenReturn(Observable.just(speakerList))
        searchService.find(QUERY)
            .test()
            .assertValue(SearchResult.Error)
    }

    companion object {
        private const val QUERY = "A"
        private const val UID = "uid"
    }
}
