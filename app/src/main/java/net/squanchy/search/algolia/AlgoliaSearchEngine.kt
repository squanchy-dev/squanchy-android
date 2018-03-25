package net.squanchy.search.algolia

import com.algolia.search.saas.Index
import com.algolia.search.saas.Query
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import net.squanchy.search.algolia.model.AlgoliaSearchResponse
import net.squanchy.search.algolia.model.AlgoliaSearchResult
import net.squanchy.search.algolia.model.AlgoliaSearchResult.Matches
import net.squanchy.search.algolia.model.AlgoliaSearchResult.DoNotFilter
import net.squanchy.search.algolia.model.AlgoliaSearchResult.ErrorSearching
import timber.log.Timber

class AlgoliaSearchEngine(
    private val eventIndex: Index,
    private val speakerIndex: Index,
    private val parser: ResponseParser<AlgoliaSearchResponse>
) {

    private val publishSubject: PublishSubject<AlgoliaSearchResult> = PublishSubject.create()

    fun query(key: String): Observable<AlgoliaSearchResult> {
        if (key.length < 2) {
            publishSubject.onNext(DoNotFilter)
        } else {
            Observable.combineLatest(eventIndex.searchAsObservable(key), speakerIndex.searchAsObservable(key), combineInPair())
                .map<AlgoliaSearchResult> { Matches(it.first.extractIds(), it.second.extractIds()) }
                .onErrorReturnItem(ErrorSearching)
                .subscribe(publishSubject)
        }
        return publishSubject
    }

    private fun Index.searchAsObservable(key: String): Observable<AlgoliaSearchResponse> {
        return Observable.create { e ->
            val request = searchAsync(Query(key)) { result, ex ->
                if (!e.isDisposed) {
                    if (result != null) {
                        val parsedResult = parser.parse(result)
                        parsedResult?.let(e::onNext)
                    } else {
                        Timber.e(ex)
                        e.onError(ex)
                    }
                }
            }
            e.setCancellable { request.cancel() }
        }
    }

    private fun combineInPair(): BiFunction<AlgoliaSearchResponse, AlgoliaSearchResponse, Pair<AlgoliaSearchResponse, AlgoliaSearchResponse>> =
        BiFunction(::Pair)

    private fun AlgoliaSearchResponse.extractIds() = hits?.map { it.objectID } ?: emptyList()
}
