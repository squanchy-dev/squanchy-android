package net.squanchy.schedule.filterschedule

import dagger.Module
import dagger.Provides

@Module
class TracksFilterModule {

    @Provides
    fun trackFilter(): TracksFilter = InMemoryTracksFilter()
}
