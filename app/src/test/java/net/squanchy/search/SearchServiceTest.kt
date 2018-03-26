package net.squanchy.search

import io.reactivex.Observable
import net.squanchy.schedule.domain.view.anEvent
import net.squanchy.search.algolia.AlgoliaSearchEngine
import net.squanchy.search.algolia.model.AlgoliaSearchResult
import net.squanchy.service.aUser
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.repository.AuthService
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.speaker.domain.view.aSpeaker
import net.squanchy.support.lang.Optional
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

    lateinit var searchService: SearchService

    lateinit var authService: FirebaseAuthService

    @Mock
    lateinit var authProvider: AuthService

    @Mock
    lateinit var eventRepository: EventRepository

    @Mock
    lateinit var speakerRepository: SpeakerRepository

    @Mock
    lateinit var algoliaSearchEngine: AlgoliaSearchEngine

    @Before
    fun setup() {
        `when`(authProvider.currentUser()).thenReturn(Observable.just(Optional.of(aUser(id = UID))))
        searchService = SearchService(eventRepository, speakerRepository, authService, algoliaSearchEngine)
    }

    @Test
    fun `should not filter the results when the query is not long enough`() {
        val eventList = listOf(anEvent(), anEvent(id = "qwer"))
        val speakerList = listOf(aSpeaker(), aSpeaker(id = "qwer"))
        `when`(algoliaSearchEngine.query(QUERY)).thenReturn(Observable.just(AlgoliaSearchResult.QueryNotLongEnough))
        `when`(eventRepository.events(UID)).thenReturn(Observable.just(eventList))
        `when`(speakerRepository.speakers()).thenReturn(Observable.just(speakerList))
        searchService.find(QUERY)
            .test()
            .assertValue(SearchResult(eventList, speakerList))
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
            .assertValue(SearchResult(listOf(anEvent(id = "qwer")), emptyList()))
    }

    companion object {
        private const val QUERY = "A"
        private const val UID = "uid"
    }
}
