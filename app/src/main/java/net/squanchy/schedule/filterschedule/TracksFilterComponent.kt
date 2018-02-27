package net.squanchy.schedule.filterschedule

import android.content.Context
import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.service.repository.TracksRepository

@ActivityLifecycle
@Component(modules = [TracksFilterModule::class], dependencies = [ApplicationComponent::class])
interface TracksFilterComponent {

    fun tracksRepository(): TracksRepository

    fun tracksFilter(): TracksFilter
}

internal fun tracksFilterComponent(context: Context): TracksFilterComponent =
    DaggerTracksFilterComponent.builder()
        .applicationComponent(context.applicationComponent)
        .tracksFilterModule(TracksFilterModule())
        .build()
