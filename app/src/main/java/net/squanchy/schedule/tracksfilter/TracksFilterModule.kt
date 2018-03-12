package net.squanchy.schedule.tracksfilter

import dagger.Module
import dagger.Provides
import net.squanchy.injection.ApplicationLifecycle
import net.squanchy.service.repository.firestore.TracksRepository

@Module
class TracksFilterModule {

    @Provides
    @ApplicationLifecycle
    fun trackFilter(tracksRepository: TracksRepository): TracksFilter = InMemoryTracksFilter(tracksRepository)
}
