package net.squanchy.schedule.tracksfilter

import dagger.Module
import dagger.Provides
import net.squanchy.injection.ApplicationLifecycle
import net.squanchy.schedule.InMemoryTracksFilter
import net.squanchy.schedule.TracksFilter
import net.squanchy.schedule.TracksRepository

@Module
class TracksFilterModule {

    @Provides
    @ApplicationLifecycle
    fun trackFilter(tracksRepository: TracksRepository): TracksFilter = InMemoryTracksFilter(
        tracksRepository
    )
}
