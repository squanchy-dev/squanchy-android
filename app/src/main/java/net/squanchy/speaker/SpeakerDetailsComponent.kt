package net.squanchy.speaker

import dagger.Component
import net.squanchy.injection.BaseActivityComponentBuilder
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

internal fun speakerDetailsComponent(activity: SpeakerDetailsActivity): SpeakerDetailsComponent =
    DaggerSpeakerDetailsComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activity(activity)
        .build()

@ActivityLifecycle
@Component(modules = [SpeakerDetailsModule::class, NavigationModule::class], dependencies = [ApplicationComponent::class])
internal interface SpeakerDetailsComponent {

    fun service(): SpeakerDetailsService

    fun navigator(): Navigator

    @Component.Builder
    interface Builder : BaseActivityComponentBuilder<SpeakerDetailsComponent>
}
