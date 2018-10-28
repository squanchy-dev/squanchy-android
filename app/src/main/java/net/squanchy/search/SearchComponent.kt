package net.squanchy.search

import dagger.Component
import net.squanchy.injection.BaseActivityComponentBuilder
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

internal fun searchComponent(activity: SearchActivity): SearchComponent =
    DaggerSearchComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activity(activity)
        .build()

@ActivityLifecycle
@Component(modules = [SearchModule::class, NavigationModule::class], dependencies = [ApplicationComponent::class])
internal interface SearchComponent {

    fun service(): SearchService

    fun navigator(): Navigator

    @Component.Builder
    interface Builder : BaseActivityComponentBuilder<SearchComponent>
}
