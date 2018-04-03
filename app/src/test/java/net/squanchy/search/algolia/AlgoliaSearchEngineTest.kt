package net.squanchy.search.algolia

import com.squareup.moshi.Moshi
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import net.squanchy.search.algolia.model.AlgoliaSearchResponse
import net.squanchy.search.algolia.model.AlgoliaSearchResult
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.io.IOException

class AlgoliaSearchEngineTest {

    @Rule
    @JvmField
    var rule: MockitoRule = MockitoJUnit.rule()

    lateinit var algoliaSearchEngine: AlgoliaSearchEngine

    lateinit var parser: ResponseParser<AlgoliaSearchResponse>

    @Mock
    private lateinit var speakerIndex: SearchIndex

    @Mock
    private lateinit var eventIndex: SearchIndex

    @Before
    fun setup() {
        parser = MoshiResponseParser(Moshi.Builder().build().adapter(AlgoliaSearchResponse::class.java))
        algoliaSearchEngine = AlgoliaSearchEngine(eventIndex, speakerIndex)

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `should return QueryNotLongEnough when query is less than 2 characters`() {
        algoliaSearchEngine.query(INVALID_QUERY)
            .test()
            .assertValue(AlgoliaSearchResult.QueryNotLongEnough)
    }

    @Test
    fun `should return the correct list with speaker and event ids when the requests are successful`() {
        `when`(speakerIndex.search(VALID_QUERY)).thenReturn(parser.parse(algoliaSpeakerResponse))
        `when`(eventIndex.search(VALID_QUERY)).thenReturn(parser.parse(algoliaEventResponse))

        algoliaSearchEngine.query(VALID_QUERY)
            .test()
            .assertValue(AlgoliaSearchResult.Matches(MATCHING_EVENT_IDS, MATCHING_SPEAKER_IDS))
    }

    @Test
    fun `should return the error object when one of the two requests fails`() {
        `when`(speakerIndex.search(VALID_QUERY)).thenReturn(parser.parse(algoliaSpeakerResponse))
        `when`(eventIndex.search(VALID_QUERY)).thenThrow(IOException(":("))

        algoliaSearchEngine.query(VALID_QUERY)
            .test()
            .assertValue(AlgoliaSearchResult.ErrorSearching)
    }

    companion object {
        private const val VALID_QUERY = "sa"
        private const val INVALID_QUERY = "a"
    }
}
