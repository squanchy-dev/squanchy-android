package net.squanchy.schedule.filterschedule

import android.content.Context
import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.service.firestore.model.schedule.TrackModule
import net.squanchy.service.firestore.model.schedule.TrackService
import net.squanchy.service.repository.TrackFilter

@ActivityLifecycle
@Component(modules = [TrackModule::class, TrackFilterModule::class], dependencies = [ApplicationComponent::class])
interface TrackFilterComponent {

    fun trackService(): TrackService

    fun trackFilter(): TrackFilter
}

internal fun trackFilterComponent(context: Context): TrackFilterComponent =
    DaggerTrackFilterComponent.builder()
        .applicationComponent(context.applicationComponent)
        .trackModule(TrackModule())
        .trackFilterModule(TrackFilterModule())
        .build()
