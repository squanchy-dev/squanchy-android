package net.squanchy.search

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.Completable
import io.reactivex.Observable
import net.squanchy.schedule.domain.view.anEvent
import net.squanchy.search.algolia.AlgoliaSearchEngine
import net.squanchy.search.algolia.model.AlgoliaSearchResult
import net.squanchy.service.repository.AuthService
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.service.repository.User
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

    @Mock
    lateinit var eventRepository: EventRepository

    @Mock
    lateinit var speakerRepository: SpeakerRepository

    @Mock
    lateinit var algoliaSearchEngine: AlgoliaSearchEngine

    @Before
    fun setup() {
        searchService = SearchService(eventRepository, speakerRepository, FakeAuthService, algoliaSearchEngine)
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
            .assertValue(SearchResult.Success(emptyList(), speakerList))
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
            .assertValue(SearchResult.Success(listOf(anEvent(id = "qwer")), emptyList()))
    }

    @Test
    fun `should receive an error when the angolia search engine fails`() {
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

    private object FakeAuthService : AuthService {

        override fun signInWithGoogle(account: GoogleSignInAccount): Completable {
            TODO("not implemented")
        }

        override fun <T> ifUserSignedInThenObservableFrom(observable: (String) -> Observable<T>): Observable<T> {
            return observable(UID)
        }

        override fun ifUserSignedInThenCompletableFrom(completable: (String) -> Completable): Completable {
            TODO("not implemented")
        }

        override fun currentUser(): Observable<Optional<User>> {
            TODO("not implemented")
        }

        override fun signOut(): Completable {
            TODO("not implemented")
        }

        override fun signInAnonymously(): Completable {
            TODO("not implemented")
        }
    }
}
