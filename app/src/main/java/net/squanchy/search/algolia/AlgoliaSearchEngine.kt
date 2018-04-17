package net.squanchy.search.algolia

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import net.squanchy.search.algolia.model.AlgoliaSearchResponse
import net.squanchy.search.algolia.model.AlgoliaSearchResult
import net.squanchy.search.algolia.model.AlgoliaSearchResult.ErrorSearching
import net.squanchy.search.algolia.model.AlgoliaSearchResult.Matches
import net.squanchy.search.algolia.model.AlgoliaSearchResult.QueryNotLongEnough
import timber.log.Timber
import java.io.IOException

class AlgoliaSearchEngine(
    private val eventIndex: SearchIndex,
    private val speakerIndex: SearchIndex
) {

    fun query(key: String): Observable<AlgoliaSearchResult> {
        val trimmedQuery = key.trim()
        if (trimmedQuery.length < QUERY_MIN_LENGTH) {
            return Observable.just(QueryNotLongEnough)
        } else {
            return Observable.combineLatest(
                eventIndex.searchAsObservable(trimmedQuery),
                speakerIndex.searchAsObservable(trimmedQuery),
                combineInPair()
            )
                .map<AlgoliaSearchResult> { Matches(it.first.extractIds(), it.second.extractIds()) }
                .doOnError(Timber::e)
                .onErrorReturnItem(ErrorSearching)
                .subscribeOn(Schedulers.io())
        }
    }

    private fun SearchIndex.searchAsObservable(key: String): Observable<AlgoliaSearchResponse> = Observable.create { emitter ->
        try {
            val result = search(key)
            if (result != null && !emitter.isDisposed) {
                emitter.onNext(result)
            }
        } catch (exception: IOException) {
            if (!emitter.isDisposed) {
                emitter.onError(exception)
            }
        }
    }

    private fun combineInPair(): BiFunction<AlgoliaSearchResponse, AlgoliaSearchResponse, Pair<AlgoliaSearchResponse, AlgoliaSearchResponse>> =
        BiFunction(::Pair)

    private fun AlgoliaSearchResponse.extractIds() = hits?.map { it.objectID } ?: emptyList()

    companion object {
        private const val QUERY_MIN_LENGTH = 2
    }
}
