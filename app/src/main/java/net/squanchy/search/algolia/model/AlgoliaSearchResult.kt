package net.squanchy.search.algolia.model

class AlgoliaSearchResult(
    val algoliaSearchHit: List<AlgoliaSearchHit>
)

class AlgoliaSearchHit(
    val objectID: String
)

data class SearchResult(
    val matchingEventIds: List<String>,
    val matchingSpeakerIds: List<String>
)
