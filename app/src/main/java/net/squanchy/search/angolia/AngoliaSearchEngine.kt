package net.squanchy.search.angolia

import com.algolia.search.saas.Index
import com.algolia.search.saas.Query
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import net.squanchy.search.angolia.model.AngoliaSearchResult
import net.squanchy.search.angolia.model.SearchResult
import timber.log.Timber

class AngoliaSearchEngine(
    private val eventIndex: Index,
    private val speakerIndex: Index
) {

    private val publishSubject: PublishSubject<SearchResult> = PublishSubject.create()

    private val moshi: Moshi = Moshi.Builder().build()
    private val adapter: JsonAdapter<AngoliaSearchResult> = moshi.adapter(AngoliaSearchResult::class.java)

    fun query(key: String): Observable<SearchResult> {
        Observable.combineLatest(eventIndex.searchAsObservable(key), speakerIndex.searchAsObservable(key), combineInPair())
            .map { SearchResult(it.first.extractIds(), it.second.extractIds()) }
            .subscribe(publishSubject)
        return publishSubject
    }

    private fun Index.searchAsObservable(key: String): Observable<AngoliaSearchResult> {
        return Observable.create { e ->
            val request = searchAsync(Query(key)) { result, error ->
                if (!e.isDisposed) {
                    if (result != null) {
                        e.onNext(adapter.fromJson(result.toString())!!)
                    } else {
                        Timber.e(error)
                        e.onNext(AngoliaSearchResult(emptyList()))
                    }
                }
            }
            e.setCancellable { request.cancel() }
        }
    }

    private fun combineInPair(): BiFunction<AngoliaSearchResult, AngoliaSearchResult, Pair<AngoliaSearchResult, AngoliaSearchResult>> =
        BiFunction(::Pair)

    private fun AngoliaSearchResult.extractIds() = angoliaSearchHit.map { it.objectID }
}
