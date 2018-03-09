package net.squanchy.home

import android.support.v7.app.AppCompatActivity
import net.squanchy.analytics.Analytics
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.support.injection.CurrentTimeModule

import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.applicationComponent

internal fun homeComponent(activity: AppCompatActivity) =
    DaggerHomeComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activityContextModule(ActivityContextModule(activity))
        .build()

@ActivityLifecycle
@Component(modules = [NavigationModule::class, CurrentTimeModule::class], dependencies = [ApplicationComponent::class])
internal interface HomeComponent {

    fun analytics(): Analytics

    fun navigator(): Navigator
}
