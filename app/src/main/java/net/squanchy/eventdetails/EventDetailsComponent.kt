package net.squanchy.eventdetails

import android.app.Activity
import dagger.BindsInstance
import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

internal fun eventDetailsComponent(activity: EventDetailsActivity): EventDetailsComponent =
    DaggerEventDetailsComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activity(activity)
        .build()

@ActivityLifecycle
@Component(modules = [EventDetailsModule::class, NavigationModule::class], dependencies = [ApplicationComponent::class])
internal interface EventDetailsComponent {

    fun service(): EventDetailsService

    fun navigator(): Navigator

    @Component.Builder
    interface Builder {
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        @BindsInstance
        fun activity(activity: Activity): Builder

        fun build(): EventDetailsComponent
    }
}
