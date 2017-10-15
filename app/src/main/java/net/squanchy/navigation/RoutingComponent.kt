package net.squanchy.navigation

import android.support.v7.app.AppCompatActivity
import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.ApplicationInjector
import net.squanchy.navigation.deeplink.DeepLinkModule
import net.squanchy.navigation.deeplink.DeepLinkRouter
import net.squanchy.onboarding.Onboarding
import net.squanchy.onboarding.OnboardingModule
import net.squanchy.signin.SignInModule
import net.squanchy.signin.SignInService

internal fun routingComponent(activity: AppCompatActivity): RoutingComponent {
    return DaggerRoutingComponent.builder()
            .activityContextModule(ActivityContextModule(activity))
            .applicationComponent(ApplicationInjector.obtain(activity))
            .deepLinkModule(DeepLinkModule())
            .navigationModule(NavigationModule())
            .signInModule(SignInModule())
            .routingModule(RoutingModule())
            .build()
}

@ActivityLifecycle
@Component(
        modules = arrayOf(DeepLinkModule::class, SignInModule::class, OnboardingModule::class, RoutingModule::class),
        dependencies = arrayOf(ApplicationComponent::class)
)
internal interface RoutingComponent {

    fun deepLinkRouter(): DeepLinkRouter

    fun navigator(): Navigator

    fun signInService(): SignInService

    fun firstStartPersister(): FirstStartPersister

    fun onboarding(): Onboarding
}
