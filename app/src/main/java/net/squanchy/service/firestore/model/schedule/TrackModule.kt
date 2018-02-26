package net.squanchy.service.firestore.model.schedule

import dagger.Module
import dagger.Provides
import net.squanchy.service.firestore.FirestoreDbService

@Module
class TrackModule {

    @Provides
    internal fun trackService(dbService: FirestoreDbService): TrackService =
        FirestoreTrackService(dbService)
}
