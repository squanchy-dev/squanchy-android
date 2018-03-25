package net.squanchy.search.algolia

import com.squareup.moshi.JsonAdapter
import org.json.JSONObject

interface ResponseParser<out T> {

    fun parse(json: JSONObject): T?
}

class MoshiResponseParser<out AlgoliaSearchResponse>(
    private val adapter: JsonAdapter<AlgoliaSearchResponse>
) : ResponseParser<AlgoliaSearchResponse> {

    override fun parse(json: JSONObject): AlgoliaSearchResponse? {
        return adapter.fromJson(json.toString())
    }
}
