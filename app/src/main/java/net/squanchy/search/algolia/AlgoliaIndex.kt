package net.squanchy.search.algolia

import com.algolia.search.saas.AlgoliaException
import com.algolia.search.saas.Index
import com.algolia.search.saas.Query
import java.io.IOException

typealias JsonString = String

interface SearchIndex {

    @Throws(IOException::class)
    fun search(key: String): JsonString?
}

class AlgoliaIndex(private val index: Index) : SearchIndex {

    override fun search(key: String): JsonString? {
        return try {
            index.searchSync(Query(key))?.toString()
        } catch (e: AlgoliaException) {
            throw IOException(e)
        }
    }
}
