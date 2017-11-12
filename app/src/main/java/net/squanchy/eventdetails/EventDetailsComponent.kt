package net.squanchy.eventdetails

import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

import dagger.Component

@ActivityLifecycle
@Component(modules = arrayOf(EventDetailsModule::class, NavigationModule::class), dependencies = arrayOf(ApplicationComponent::class))
internal interface EventDetailsComponent {

    fun service(): EventDetailsService

    fun navigator(): Navigator
}
