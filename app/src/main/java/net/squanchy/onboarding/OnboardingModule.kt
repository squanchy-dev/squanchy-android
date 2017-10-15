package net.squanchy.onboarding

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import net.squanchy.injection.ActivityContextModule

@Module(includes = arrayOf(ActivityContextModule::class))
class OnboardingModule {

    @Provides
    internal fun onboardingPersister(activity: Activity): OnboardingPersister {
        // The preferences cannot be @Provide'd because we need different ones in different places
        // (but they are all SharedPreferences) and the Persister is our abstraction of preferences.
        val preferences = activity.getSharedPreferences(ONBOARDING_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return OnboardingPersister(preferences)
    }

    @Provides
    fun onboarding(persister: OnboardingPersister): Onboarding {
        return Onboarding(persister)
    }

    companion object {

        private val ONBOARDING_PREFERENCES_NAME = "onboarding"
    }
}
