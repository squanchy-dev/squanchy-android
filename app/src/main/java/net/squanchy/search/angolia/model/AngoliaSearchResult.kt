package net.squanchy.search.angolia.model

class AngoliaSearchResult(
    val angoliaSearchHit: List<AngoliaSearchHit>
)

class AngoliaSearchHit(
    val objectID: String
)

data class SearchResult(
    val matchingEventIds: List<String>,
    val matchingSpeakerIds: List<String>
)