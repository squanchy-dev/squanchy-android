package net.squanchy.eventdetails

import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.applicationComponent

internal fun eventDetailsComponent(activity: EventDetailsActivity): EventDetailsComponent {
    return DaggerEventDetailsComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .eventDetailsModule(EventDetailsModule())
        .activityContextModule(ActivityContextModule(activity))
        .navigationModule(NavigationModule())
        .build()
}

@ActivityLifecycle
@Component(modules = arrayOf(EventDetailsModule::class, NavigationModule::class), dependencies = arrayOf(ApplicationComponent::class))
internal interface EventDetailsComponent {

    fun service(): EventDetailsService

    fun navigator(): Navigator
}
