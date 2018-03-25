package net.squanchy.search.algolia

import com.algolia.search.saas.AlgoliaException
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import net.squanchy.search.algolia.model.AlgoliaSearchResponse
import net.squanchy.search.algolia.model.AlgoliaSearchResult
import net.squanchy.search.algolia.model.AlgoliaSearchResult.ErrorSearching
import net.squanchy.search.algolia.model.AlgoliaSearchResult.QueryNotLongEnough
import net.squanchy.search.algolia.model.AlgoliaSearchResult.Matches
import timber.log.Timber

class AlgoliaSearchEngine(
    private val eventIndex: SearchIndex,
    private val speakerIndex: SearchIndex,
    private val parser: ResponseParser<AlgoliaSearchResponse>
) {

    fun query(key: String): Observable<AlgoliaSearchResult> {
        if (key.length < 2) {
            return Observable.just(QueryNotLongEnough)
        } else {
            return Observable.combineLatest(eventIndex.searchAsObservable(key), speakerIndex.searchAsObservable(key), combineInPair())
                .map<AlgoliaSearchResult> { Matches(it.first.extractIds(), it.second.extractIds()) }
                .onErrorReturnItem(ErrorSearching)
        }
    }

    private fun SearchIndex.searchAsObservable(key: String): Observable<AlgoliaSearchResponse> {
        return Observable.create { emitter ->
            try {
                val result = search(key)
                if (!emitter.isDisposed && result != null) {
                    val parsedResult = parser.parse(result)
                    parsedResult?.let(emitter::onNext)
                }
            } catch (ex: AlgoliaException) {
                Timber.e(ex)
                if (!emitter.isDisposed) {
                    emitter.onError(ex)
                }
            }
        }
    }

    private fun combineInPair(): BiFunction<AlgoliaSearchResponse, AlgoliaSearchResponse, Pair<AlgoliaSearchResponse, AlgoliaSearchResponse>> =
        BiFunction(::Pair)

    private fun AlgoliaSearchResponse.extractIds() = hits?.map { it.objectID } ?: emptyList()
}
