package net.squanchy.search.algolia

import android.app.Application
import com.algolia.search.saas.Client
import com.algolia.search.saas.Index
import dagger.Module
import dagger.Provides
import net.squanchy.R
import net.squanchy.injection.ApplicationLifecycle
import javax.inject.Named

@Module
class AlgoliaModule {

    @Provides
    @ApplicationLifecycle
    fun algoliaClient(app: Application) = Client(app.getString(R.string.algolia_application_id), app.getString(R.string.algolia_api_key))

    @Provides
    @ApplicationLifecycle
    @Named(EVENT_INDEX)
    fun algoliaEventIndex(client: Client) = client.getIndex(EVENT_INDEX)

    @Provides
    @ApplicationLifecycle
    @Named(SPEAKER_INDEX)
    fun algoliaSpeakerIndex(client: Client) = client.getIndex(SPEAKER_INDEX)

    @Provides
    @ApplicationLifecycle
    fun algoliaSearchEngine(@Named(EVENT_INDEX) eventIndex: Index, @Named(SPEAKER_INDEX) speakerIndex: Index) =
        AlgoliaSearchEngine(eventIndex, speakerIndex)

    companion object {
        const val EVENT_INDEX = "squanchy_dev-events"
        const val SPEAKER_INDEX = "squanchy_dev-speakers"
    }
}
