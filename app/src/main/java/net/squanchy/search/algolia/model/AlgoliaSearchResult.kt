package net.squanchy.search.algolia.model

class AlgoliaSearchResult(
    val hits: List<AlgoliaSearchHit>?
)

class AlgoliaSearchHit(
    val objectID: String
)

data class AlgoliaMatches(
    val eventIds: List<String>,
    val speakerIds: List<String>
)
