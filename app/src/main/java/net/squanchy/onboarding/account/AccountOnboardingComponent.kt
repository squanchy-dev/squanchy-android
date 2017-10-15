package net.squanchy.onboarding.account

import android.support.v7.app.AppCompatActivity
import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.ApplicationInjector
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.onboarding.Onboarding
import net.squanchy.onboarding.OnboardingModule
import net.squanchy.signin.SignInModule
import net.squanchy.signin.SignInService

internal fun accountOnboardingComponent(activity: AppCompatActivity): AccountOnboardingComponent {
    return DaggerAccountOnboardingComponent.builder()
            .activityContextModule(ActivityContextModule(activity))
            .applicationComponent(ApplicationInjector.obtain(activity))
            .build()
}

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
