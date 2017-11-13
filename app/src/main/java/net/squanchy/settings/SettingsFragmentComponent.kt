package net.squanchy.settings

import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.remoteconfig.RemoteConfig
import net.squanchy.signin.SignInModule
import net.squanchy.signin.SignInService

import dagger.Component

@ActivityLifecycle
@Component(modules = arrayOf(SignInModule::class, NavigationModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface SettingsFragmentComponent {

    fun navigator(): Navigator

    fun signInService(): SignInService

    fun remoteConfig(): RemoteConfig
}
