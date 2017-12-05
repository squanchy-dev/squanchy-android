package net.squanchy.favorites

import android.support.v7.app.AppCompatActivity
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.createApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.schedule.ScheduleModule
import net.squanchy.schedule.ScheduleService

@ActivityLifecycle
@Component(modules = arrayOf(ScheduleModule::class, NavigationModule::class), dependencies = arrayOf(ApplicationComponent::class))
internal interface FavoritesComponent {

    fun service(): ScheduleService

    fun navigator(): Navigator

    fun analytics(): Analytics
}

internal fun favoritesComponent(activity: AppCompatActivity): FavoritesComponent {
    return DaggerFavoritesComponent.builder()
            .applicationComponent(createApplicationComponent(activity.application))
            .scheduleModule(ScheduleModule())
            .navigationModule(NavigationModule())
            .activityContextModule(ActivityContextModule(activity))
            .build()
}
