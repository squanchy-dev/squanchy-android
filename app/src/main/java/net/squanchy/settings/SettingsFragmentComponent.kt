package net.squanchy.settings

import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.remoteconfig.RemoteConfig
import net.squanchy.signin.SignInModule
import net.squanchy.signin.SignInService
import net.squanchy.wificonfig.WifiConfigModule
import net.squanchy.wificonfig.WifiConfigService

internal fun settingsFragmentComponent(activity: AppCompatActivity) =
    DaggerSettingsFragmentComponent.builder()
        .activityContextModule(ActivityContextModule(activity))
        .applicationComponent(activity.applicationComponent)
        .navigationModule(NavigationModule())
        .signInModule(SignInModule())
        .wifiConfigModule(WifiConfigModule())
        .build()

@ActivityLifecycle
@Component(modules = [SignInModule::class, NavigationModule::class, WifiConfigModule::class], dependencies = [ApplicationComponent::class])
interface SettingsFragmentComponent {

    fun navigator(): Navigator

    fun signInService(): SignInService

    fun analytics(): Analytics

    fun remoteConfig(): RemoteConfig

    fun wifiConfigService(): WifiConfigService
}
