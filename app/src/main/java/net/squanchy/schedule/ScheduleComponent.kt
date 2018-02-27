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
import net.squanchy.schedule.filterschedule.TracksFilterModule
import net.squanchy.support.injection.CurrentTimeModule
import net.squanchy.support.system.CurrentTime

@ActivityLifecycle
@Component(modules = [
    ScheduleModule::class,
    NavigationModule::class,
    CurrentTimeModule::class,
    TracksFilterModule::class
], dependencies = [ApplicationComponent::class])
internal interface ScheduleComponent {

    fun service(): ScheduleService

    fun navigator(): Navigator

    fun analytics(): Analytics

    fun currentTime(): CurrentTime
}

internal fun scheduleComponent(activity: AppCompatActivity): ScheduleComponent = DaggerScheduleComponent.builder()
    .applicationComponent(activity.applicationComponent)
    .scheduleModule(ScheduleModule())
    .navigationModule(NavigationModule())
    .tracksFilterModule(TracksFilterModule())
    .activityContextModule(ActivityContextModule(activity))
    .currentTimeModule(CurrentTimeModule())
    .build()
