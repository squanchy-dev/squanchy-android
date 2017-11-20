package net.squanchy.about

import net.squanchy.injection.ActivityLifecycle
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

import dagger.Component

@ActivityLifecycle
@Component(modules = arrayOf(NavigationModule::class))
interface AboutComponent {

    fun navigator(): Navigator
}
