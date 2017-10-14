package net.squanchy.onboarding.account

import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.onboarding.Onboarding
import net.squanchy.onboarding.OnboardingModule
import net.squanchy.signin.SignInModule
import net.squanchy.signin.SignInService

@ActivityLifecycle
@Component(
        modules = arrayOf(OnboardingModule::class, SignInModule::class, NavigationModule::class),
        dependencies = arrayOf(ApplicationComponent::class)
)
internal interface AccountOnboardingComponent {

    fun onboarding(): Onboarding

    fun signInService(): SignInService

    fun navigator(): Navigator
}
