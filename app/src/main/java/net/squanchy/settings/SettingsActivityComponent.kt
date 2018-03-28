package net.squanchy.settings

import android.app.Activity
import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.signin.SignInModule
import net.squanchy.signin.SignInService

internal fun settingsActivityComponent(activity: Activity) =
    DaggerSettingsActivityComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .signInModule(SignInModule())
        .build()

@ActivityLifecycle
@Component(modules = [SignInModule::class], dependencies = [ApplicationComponent::class])
internal interface SettingsActivityComponent {

    fun signInService(): SignInService

}
