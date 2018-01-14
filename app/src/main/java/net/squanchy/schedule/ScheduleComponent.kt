package net.squanchy.schedule

import android.support.v7.app.AppCompatActivity
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

@ActivityLifecycle
@Component(modules = [ScheduleModule::class, NavigationModule::class], dependencies = [ApplicationComponent::class])
internal interface ScheduleComponent {

    fun service(): ScheduleService

    fun navigator(): Navigator

    fun analytics(): Analytics
}

internal fun scheduleComponent(activity: AppCompatActivity): ScheduleComponent = DaggerScheduleComponent.builder()
    .applicationComponent(activity.applicationComponent)
    .scheduleModule(ScheduleModule())
    .navigationModule(NavigationModule())
    .activityContextModule(ActivityContextModule(activity))
    .build()
