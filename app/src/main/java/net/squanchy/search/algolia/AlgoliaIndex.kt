package net.squanchy.search.algolia

import com.algolia.search.saas.AlgoliaException
import com.algolia.search.saas.Index
import com.algolia.search.saas.Query
import net.squanchy.search.algolia.model.AlgoliaSearchResponse
import java.io.IOException

interface SearchIndex {

    @Throws(IOException::class)
    fun search(key: String): AlgoliaSearchResponse?
}

class AlgoliaIndex(private val index: Index, private val parser: ResponseParser<AlgoliaSearchResponse>) : SearchIndex {

    override fun search(key: String): AlgoliaSearchResponse? {
        return try {
            parser.parse(index.searchSync(Query(key)).toString())
        } catch (e: AlgoliaException) {
            throw IOException(e)
        }
    }
}
