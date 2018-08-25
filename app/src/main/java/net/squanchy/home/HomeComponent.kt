package net.squanchy.home

import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.support.injection.CurrentTimeModule

fun homeComponent(activity: AppCompatActivity) =
    DaggerHomeComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activityContextModule(ActivityContextModule(activity))
        .build()

@ActivityLifecycle
@Component(modules = [NavigationModule::class, CurrentTimeModule::class], dependencies = [ApplicationComponent::class])
interface HomeComponent {

    fun analytics(): Analytics

    fun navigator(): Navigator
}
