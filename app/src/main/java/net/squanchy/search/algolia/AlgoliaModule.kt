package net.squanchy.search.algolia

import android.app.Application
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
    fun algoliaEventIndex(client: Client) = AlgoliaIndex(client.getIndex(EVENT_INDEX))

    @Provides
    @ApplicationLifecycle
    @Named(SPEAKER_INDEX)
    fun algoliaSpeakerIndex(client: Client) = AlgoliaIndex(client.getIndex(SPEAKER_INDEX))

    @Provides
    @ApplicationLifecycle
    fun algoliaSearchEngine(
        @Named(EVENT_INDEX) eventIndex: AlgoliaIndex,
        @Named(SPEAKER_INDEX) speakerIndex: AlgoliaIndex,
        parser: ResponseParser<AlgoliaSearchResponse>
    ): AlgoliaSearchEngine {
        return AlgoliaSearchEngine(eventIndex, speakerIndex, parser)
    }

    @Provides
    @ApplicationLifecycle
    fun moshi() = Moshi.Builder().build()

    @Provides
    @ApplicationLifecycle
    fun algoliaResultAdapter(moshi: Moshi): JsonAdapter<AlgoliaSearchResponse> = moshi.adapter(AlgoliaSearchResponse::class.java)

    @Provides
    fun responseParser(adapter: JsonAdapter<AlgoliaSearchResponse>): ResponseParser<AlgoliaSearchResponse> = MoshiResponseParser(adapter)

    companion object {
        const val EVENT_INDEX = "squanchy_dev-events"
        const val SPEAKER_INDEX = "squanchy_dev-speakers"
    }
}
