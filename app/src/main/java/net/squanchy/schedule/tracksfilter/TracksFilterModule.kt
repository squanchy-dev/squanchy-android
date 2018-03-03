package net.squanchy.schedule.tracksfilter

import dagger.Module
import dagger.Provides
import net.squanchy.service.repository.TracksRepository

@Module
class TracksFilterModule {

    @Provides
    fun trackFilter(tracksRepository: TracksRepository): TracksFilter = InMemoryTracksFilter(tracksRepository)
}
