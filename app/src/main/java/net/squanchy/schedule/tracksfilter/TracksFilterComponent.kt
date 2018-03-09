package net.squanchy.schedule.tracksfilter

import android.content.Context
import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent

@ActivityLifecycle
@Component(dependencies = [ApplicationComponent::class])
interface TracksFilterComponent {

    fun tracksRepository(): TracksRepository

    fun tracksFilter(): TracksFilter
}

internal fun tracksFilterComponent(context: Context): TracksFilterComponent =
    DaggerTracksFilterComponent.builder()
        .applicationComponent(context.applicationComponent)
        .build()
