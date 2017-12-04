package net.squanchy.speaker

import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.applicationComponent

internal fun speakerDetailsComponent(activity: SpeakerDetailsActivity) =
    DaggerSpeakerDetailsComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .speakerDetailsModule(SpeakerDetailsModule())
        .activityContextModule(ActivityContextModule(activity))
        .navigationModule(NavigationModule())
        .build()

@ActivityLifecycle
@Component(modules = [SpeakerDetailsModule::class, NavigationModule::class], dependencies = [ApplicationComponent::class])
internal interface SpeakerDetailsComponent {

    fun service(): SpeakerDetailsService

    fun navigator(): Navigator
}
