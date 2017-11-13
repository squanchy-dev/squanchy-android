package net.squanchy.home

import net.squanchy.analytics.Analytics
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.support.injection.CurrentTimeModule

import dagger.Component

@ActivityLifecycle
@Component(modules = arrayOf(NavigationModule::class, CurrentTimeModule::class), dependencies = arrayOf(ApplicationComponent::class))
internal interface HomeComponent {

    fun analytics(): Analytics

    fun navigator(): Navigator
}
