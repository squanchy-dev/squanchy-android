package net.squanchy.settings

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

internal fun wifiConfigErrorActivityComponent(activity: AppCompatActivity): WifiConfigErrorActivityComponent =
    DaggerWifiConfigErrorActivityComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activity(activity)
        .build()

@ActivityLifecycle
@Component(modules = [NavigationModule::class], dependencies = [ApplicationComponent::class])
interface WifiConfigErrorActivityComponent {

    fun navigator(): Navigator

    @Component.Builder
    interface Builder {
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        @BindsInstance
        fun activity(activity: Activity): Builder

        fun build(): WifiConfigErrorActivityComponent
    }
}
