package net.squanchy.navigation

import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import net.squanchy.injection.BaseActivityComponentBuilder
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.deeplink.DeepLinkModule
import net.squanchy.navigation.deeplink.DeepLinkRouter
import net.squanchy.onboarding.Onboarding
import net.squanchy.onboarding.OnboardingModule
import net.squanchy.signin.SignInModule
import net.squanchy.signin.SignInService

internal fun routingComponent(activity: AppCompatActivity): RoutingComponent =
    DaggerRoutingComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activity(activity)
        .build()

@ActivityLifecycle
@Component(
    modules = [DeepLinkModule::class, SignInModule::class, OnboardingModule::class, RoutingModule::class],
    dependencies = [ApplicationComponent::class]
)
internal interface RoutingComponent {

    fun deepLinkRouter(): DeepLinkRouter

    fun navigator(): Navigator

    fun signInService(): SignInService

    fun firstStartPersister(): FirstStartPersister

    fun onboarding(): Onboarding

    @Component.Builder
    interface Builder : BaseActivityComponentBuilder<RoutingComponent>
}
