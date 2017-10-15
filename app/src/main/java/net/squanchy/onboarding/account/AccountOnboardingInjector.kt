package net.squanchy.onboarding.account

import android.support.v7.app.AppCompatActivity

import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ApplicationInjector

internal object AccountOnboardingInjector {

    fun obtain(activity: AppCompatActivity): AccountOnboardingComponent {
        return DaggerAccountOnboardingComponent.builder()
                .activityContextModule(ActivityContextModule(activity))
                .applicationComponent(ApplicationInjector.obtain(activity))
                .build()
    }
}
