package net.squanchy.search.angolia

import android.app.Application
import com.algolia.search.saas.Client
import com.algolia.search.saas.Index
import dagger.Module
import dagger.Provides
import net.squanchy.R
import net.squanchy.injection.ApplicationLifecycle
import javax.inject.Named

@Module
class AngoliaModule {

    @Provides
    @ApplicationLifecycle
    fun angoliaClient(app: Application) = Client(app.getString(R.string.algolia_application_id), app.getString(R.string.algolia_api_key))

    @Provides
    @ApplicationLifecycle
    @Named(EVENT_INDEX)
    fun angoliaEventIndex(client: Client) = client.getIndex(EVENT_INDEX)

    @Provides
    @ApplicationLifecycle
    @Named(SPEAKER_INDEX)
    fun angoliaSpeakerIndex(client: Client) = client.getIndex(SPEAKER_INDEX)

    @Provides
    @ApplicationLifecycle
    fun angoliaSearchEngine(@Named(EVENT_INDEX) eventIndex: Index, @Named(SPEAKER_INDEX) speakerIndex: Index) =
        AngoliaSearchEngine(eventIndex, speakerIndex)

    companion object {
        const val EVENT_INDEX = "squanchy_dev-events"
        const val SPEAKER_INDEX = "squanchy_dev-speakers"
    }
}