package net.squanchy.search.algolia.model

class AlgoliaSearchResponse(
    val hits: List<AlgoliaSearchHit>?
)

class AlgoliaSearchHit(
    val objectID: String
)

sealed class AlgoliaSearchResult {
    object QueryNotLongEnough : AlgoliaSearchResult()
    object ErrorSearching : AlgoliaSearchResult()
    data class Matches(val eventIds: List<String>, val speakerIds: List<String>) : AlgoliaSearchResult()
}
