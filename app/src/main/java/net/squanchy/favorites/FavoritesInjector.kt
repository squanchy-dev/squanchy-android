package net.squanchy.favorites

import android.support.v7.app.AppCompatActivity

import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.schedule.ScheduleModule

internal fun favouritesComponent(activity: AppCompatActivity) =
    DaggerFavoritesComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .scheduleModule(ScheduleModule())
        .navigationModule(NavigationModule())
        .activityContextModule(ActivityContextModule(activity))
        .build()
