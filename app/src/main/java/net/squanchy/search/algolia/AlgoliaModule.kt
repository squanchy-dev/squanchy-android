package net.squanchy.search.algolia

import android.app.Application
import android.content.Context
import com.algolia.search.saas.Client
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import net.squanchy.R
import net.squanchy.injection.ApplicationLifecycle
import net.squanchy.search.algolia.model.AlgoliaSearchResponse
import javax.inject.Named

@Module
class AlgoliaModule {

    @Provides
    @ApplicationLifecycle
    fun algoliaClient(app: Application) = Client(app.getString(R.string.algolia_application_id), app.getString(R.string.algolia_api_key))

    @Provides
    @ApplicationLifecycle
    @Named(EVENT_INDEX)
    fun algoliaEventIndex(app: Application, client: Client, parser: ResponseParser<AlgoliaSearchResponse>): AlgoliaIndex {
        val indexName = composeIndexName(app, EVENT_INDEX)
        return AlgoliaIndex(client.getIndex(indexName), parser)
    }

    @Provides
    @ApplicationLifecycle
    @Named(SPEAKER_INDEX)
    fun algoliaSpeakerIndex(app: Application, client: Client, parser: ResponseParser<AlgoliaSearchResponse>): AlgoliaIndex {
        val indexName = composeIndexName(app, SPEAKER_INDEX)
        return AlgoliaIndex(client.getIndex(indexName), parser)
    }
    private fun composeIndexName(app: Context, indexName: String): String {
        val indexPrefix = app.getString(R.string.algolia_indices_prefix)
        return "$indexPrefix-$indexName"
    }

    @Provides
    @ApplicationLifecycle
    fun algoliaSearchEngine(
        @Named(EVENT_INDEX) eventIndex: AlgoliaIndex,
        @Named(SPEAKER_INDEX) speakerIndex: AlgoliaIndex
    ): AlgoliaSearchEngine {
        return AlgoliaSearchEngine(eventIndex, speakerIndex)
    }

    @Provides
    @ApplicationLifecycle
    fun moshi(): Moshi = Moshi.Builder().build()

    @Provides
    @ApplicationLifecycle
    fun algoliaResultAdapter(moshi: Moshi): JsonAdapter<AlgoliaSearchResponse> = moshi.adapter(AlgoliaSearchResponse::class.java)

    @Provides
    fun responseParser(adapter: JsonAdapter<AlgoliaSearchResponse>): ResponseParser<AlgoliaSearchResponse> = MoshiResponseParser(adapter)

    companion object {
        const val EVENT_INDEX = "events"
        const val SPEAKER_INDEX = "speakers"
    }
}
