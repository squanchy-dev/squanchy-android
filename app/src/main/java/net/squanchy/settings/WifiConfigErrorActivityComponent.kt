package net.squanchy.settings

import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

internal fun wifiConfigErrorActivityComponent(activity: AppCompatActivity) =
    DaggerWifiConfigErrorActivityComponent.builder()
        .activityContextModule(ActivityContextModule(activity))
        .applicationComponent(activity.applicationComponent)
        .navigationModule(NavigationModule())
        .build()

@ActivityLifecycle
@Component(modules = [NavigationModule::class], dependencies = [ApplicationComponent::class])
interface WifiConfigErrorActivityComponent {

    fun navigator(): Navigator
}
