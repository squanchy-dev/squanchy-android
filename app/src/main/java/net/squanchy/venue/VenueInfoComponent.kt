package net.squanchy.venue

import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

@ActivityLifecycle
@Component(modules = arrayOf(VenueInfoModule::class, NavigationModule::class), dependencies = arrayOf(ApplicationComponent::class))
internal interface VenueInfoComponent {

    fun navigator(): Navigator

    fun service(): VenueInfoService
}
