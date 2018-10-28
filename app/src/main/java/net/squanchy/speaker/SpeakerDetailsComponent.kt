package net.squanchy.speaker

import android.app.Activity
import dagger.BindsInstance
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

import dagger.Component
import net.squanchy.injection.applicationComponent

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
    interface Builder {
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        @BindsInstance
        fun activity(activity: Activity): Builder

        fun build(): SpeakerDetailsComponent
    }
}
