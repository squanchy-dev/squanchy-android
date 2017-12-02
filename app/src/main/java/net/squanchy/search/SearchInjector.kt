package net.squanchy.search

import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule

internal fun searchComponent(activity: SearchActivity) =
    DaggerSearchComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .searchModule(SearchModule())
        .activityContextModule(ActivityContextModule(activity))
        .navigationModule(NavigationModule())
        .build()
