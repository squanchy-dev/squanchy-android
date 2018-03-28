package net.squanchy.eventdetails

import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

internal fun eventDetailsComponent(activity: EventDetailsActivity) =
    DaggerEventDetailsComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .eventDetailsModule(EventDetailsModule())
        .activityContextModule(ActivityContextModule(activity))
        .navigationModule(NavigationModule())
        .build()

@ActivityLifecycle
@Component(modules = [EventDetailsModule::class, NavigationModule::class], dependencies = [ApplicationComponent::class])
internal interface EventDetailsComponent {

    fun service(): EventDetailsService

    fun navigator(): Navigator

    fun analytics(): Analytics
}
