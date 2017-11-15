package net.squanchy.settings

import android.support.v7.app.AppCompatActivity
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.remoteconfig.RemoteConfig
import net.squanchy.signin.SignInModule
import net.squanchy.signin.SignInService

import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.applicationComponent

internal fun settingsFragmentComponent(activity: AppCompatActivity): SettingsFragmentComponent {
    return DaggerSettingsFragmentComponent.builder()
        .activityContextModule(ActivityContextModule(activity))
        .applicationComponent(activity.applicationComponent)
        .navigationModule(NavigationModule())
        .signInModule(SignInModule())
        .build()
}

@ActivityLifecycle
@Component(modules = arrayOf(SignInModule::class, NavigationModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface SettingsFragmentComponent {

    fun navigator(): Navigator

    fun signInService(): SignInService

    fun remoteConfig(): RemoteConfig
}
