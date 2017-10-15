package net.squanchy.onboarding.loldon

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

internal fun loldonOnboardingComponent(activity: AppCompatActivity): LoldonOnboardingComponent {
    return DaggerLoldonOnboardingComponent.builder()
            .activityContextModule(ActivityContextModule(activity))
            .applicationComponent(ApplicationInjector.obtain(activity))
            .build()
}

@ActivityLifecycle
@Component(
        modules = arrayOf(OnboardingModule::class, NavigationModule::class),
        dependencies = arrayOf(ApplicationComponent::class)
)
internal interface LoldonOnboardingComponent {

    fun onboarding(): Onboarding

    fun navigator(): Navigator
}
