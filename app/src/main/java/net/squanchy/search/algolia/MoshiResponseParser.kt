package net.squanchy.search.algolia

import com.squareup.moshi.JsonAdapter

interface ResponseParser<out T> {

    fun parse(json: String): T?
}

class MoshiResponseParser<out AlgoliaSearchResponse>(
    private val adapter: JsonAdapter<AlgoliaSearchResponse>
) : ResponseParser<AlgoliaSearchResponse> {

    override fun parse(json: String): AlgoliaSearchResponse? {
        return adapter.fromJson(json)
    }
}
