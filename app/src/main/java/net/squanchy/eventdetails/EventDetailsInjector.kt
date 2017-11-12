package net.squanchy.eventdetails

import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ApplicationInjector
import net.squanchy.navigation.NavigationModule

internal fun eventDetailsComponent(activity: EventDetailsActivity) : EventDetailsComponent {
    return DaggerEventDetailsComponent.builder()
        .applicationComponent(ApplicationInjector.obtain(activity))
        .eventDetailsModule(EventDetailsModule())
        .activityContextModule(ActivityContextModule(activity))
        .navigationModule(NavigationModule())
        .build()
}