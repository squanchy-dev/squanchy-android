package net.squanchy.settings

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import net.squanchy.analytics.Analytics
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

internal fun settingsFragmentComponent(activity: AppCompatActivity): SettingsFragmentComponent =
    DaggerSettingsFragmentComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activity(activity)
        .build()

@ActivityLifecycle
@Component(modules = [SignInModule::class, NavigationModule::class, WifiConfigModule::class], dependencies = [ApplicationComponent::class])
interface SettingsFragmentComponent {

    fun navigator(): Navigator

    fun signInService(): SignInService

    fun analytics(): Analytics

    fun remoteConfig(): RemoteConfig

    fun wifiConfigService(): WifiConfigService

    @Component.Builder
    interface Builder {
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        @BindsInstance
        fun activity(activity: Activity): Builder

        fun build(): SettingsFragmentComponent
    }
}
