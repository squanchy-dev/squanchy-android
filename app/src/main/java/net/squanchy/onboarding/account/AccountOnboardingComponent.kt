package net.squanchy.onboarding.account

import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.onboarding.Onboarding
import net.squanchy.onboarding.OnboardingModule
import net.squanchy.signin.SignInModule
import net.squanchy.signin.SignInService

internal fun accountOnboardingComponent(activity: AppCompatActivity) =
    DaggerAccountOnboardingComponent.builder()
        .activityContextModule(ActivityContextModule(activity))
        .applicationComponent(activity.applicationComponent)
        .build()

@ActivityLifecycle
@Component(
    modules = [OnboardingModule::class, SignInModule::class, NavigationModule::class],
    dependencies = [ApplicationComponent::class]
)
internal interface AccountOnboardingComponent {

    fun onboarding(): Onboarding

    fun signInService(): SignInService

    fun navigator(): Navigator
}
