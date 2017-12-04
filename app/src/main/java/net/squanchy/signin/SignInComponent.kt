package net.squanchy.signin

import android.app.Activity
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent

import dagger.Component
import net.squanchy.injection.applicationComponent

fun signInComponent(activity: Activity) =
    DaggerSignInComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .signInModule(SignInModule())
        .build()

@ActivityLifecycle
@Component(modules = [SignInModule::class], dependencies = [ApplicationComponent::class])
interface SignInComponent {

    fun service(): SignInService
}
