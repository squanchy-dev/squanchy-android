package net.squanchy.search.algolia

import com.algolia.search.saas.Index
import com.algolia.search.saas.Query
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import net.squanchy.search.algolia.model.AlgoliaSearchResult
import net.squanchy.search.algolia.model.SearchResult
import timber.log.Timber

class AlgoliaSearchEngine(
    private val eventIndex: Index,
    private val speakerIndex: Index
) {

    private val publishSubject: PublishSubject<SearchResult> = PublishSubject.create()

    private val moshi: Moshi = Moshi.Builder().build()
    private val adapter: JsonAdapter<AlgoliaSearchResult> = moshi.adapter(AlgoliaSearchResult::class.java)

    fun query(key: String): Observable<SearchResult> {
        Observable.combineLatest(eventIndex.searchAsObservable(key), speakerIndex.searchAsObservable(key), combineInPair())
            .map { SearchResult(it.first.extractIds(), it.second.extractIds()) }
            .subscribe(publishSubject)
        return publishSubject
    }

    private fun Index.searchAsObservable(key: String): Observable<AlgoliaSearchResult> {
        return Observable.create { e ->
            val request = searchAsync(Query(key)) { result, error ->
                if (!e.isDisposed) {
                    if (result != null) {
                        e.onNext(adapter.fromJson(result.toString())!!)
                    } else {
                        Timber.e(error)
                        e.onNext(AlgoliaSearchResult(emptyList()))
                    }
                }
            }
            e.setCancellable { request.cancel() }
        }
    }

    private fun combineInPair(): BiFunction<AlgoliaSearchResult, AlgoliaSearchResult, Pair<AlgoliaSearchResult, AlgoliaSearchResult>> =
        BiFunction(::Pair)

    private fun AlgoliaSearchResult.extractIds() = algoliaSearchHit.map { it.objectID }
}
