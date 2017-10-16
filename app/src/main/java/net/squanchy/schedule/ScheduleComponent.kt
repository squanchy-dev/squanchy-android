package net.squanchy.schedule

import android.support.v7.app.AppCompatActivity
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.ApplicationInjector
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.support.font.TypefaceController

@ActivityLifecycle
@Component(modules = arrayOf(ScheduleModule::class, NavigationModule::class), dependencies = arrayOf(ApplicationComponent::class))
internal interface ScheduleComponent {

    fun service(): ScheduleService

    fun navigator(): Navigator

    fun analytics(): Analytics

    fun typefaceController(): TypefaceController
}

internal fun obtain(activity: AppCompatActivity): ScheduleComponent = DaggerScheduleComponent.builder()
        .applicationComponent(ApplicationInjector.obtain(activity))
        .scheduleModule(ScheduleModule())
        .navigationModule(NavigationModule())
        .activityContextModule(ActivityContextModule(activity))
        .build()
