package net.squanchy.schedule.tracksfilter

import dagger.Module
import dagger.Provides
import net.squanchy.injection.ApplicationLifecycle

@Module
class TracksFilterModule {

    @Provides
    @ApplicationLifecycle
    fun trackFilter(tracksRepository: TracksRepository): TracksFilter = InMemoryTracksFilter(tracksRepository)
}
