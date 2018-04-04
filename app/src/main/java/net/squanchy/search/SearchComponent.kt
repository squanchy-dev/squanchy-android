package net.squanchy.search

import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.applicationComponent

internal fun searchComponent(activity: SearchActivity) =
    DaggerSearchComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .searchModule(SearchModule())
        .activityContextModule(ActivityContextModule(activity))
        .navigationModule(NavigationModule())
        .build()

@ActivityLifecycle
@Component(modules = [SearchModule::class, NavigationModule::class], dependencies = [ApplicationComponent::class])
internal interface SearchComponent {

    fun service(): SearchService

    fun navigator(): Navigator
}
