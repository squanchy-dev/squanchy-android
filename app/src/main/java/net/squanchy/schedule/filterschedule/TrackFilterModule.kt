package net.squanchy.schedule.filterschedule

import dagger.Module
import dagger.Provides
import net.squanchy.service.repository.InMemoryTrackFilter
import net.squanchy.service.repository.TrackFilter

@Module
class TrackFilterModule {

    @Provides
    fun trackFilter(): TrackFilter = InMemoryTrackFilter()
}
