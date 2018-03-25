package net.squanchy.search.algolia

import com.squareup.moshi.JsonAdapter

interface ResponseParser<out T> {

    fun parse(json: JsonString): T?
}

class MoshiResponseParser<out AlgoliaSearchResponse>(
    private val adapter: JsonAdapter<AlgoliaSearchResponse>
) : ResponseParser<AlgoliaSearchResponse> {

    override fun parse(json: JsonString): AlgoliaSearchResponse? {
        return adapter.fromJson(json)
    }
}
