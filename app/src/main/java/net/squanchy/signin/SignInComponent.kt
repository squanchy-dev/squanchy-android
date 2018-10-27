package net.squanchy.signin

import android.app.Activity
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent

fun signInComponent(activity: Activity): SignInComponent =
    DaggerSignInComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .build()

@ActivityLifecycle
@Component(modules = [SignInModule::class], dependencies = [ApplicationComponent::class])
interface SignInComponent {

    fun service(): SignInService

    fun analytics(): Analytics
}
