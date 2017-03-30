package net.squanchy.onboarding;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import net.squanchy.injection.ActivityContextModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = ActivityContextModule.class)
public class OnboardingModule {

    private static final String ONBOARDING_PREFERENCES_NAME = "onboarding";

    @Provides
    SharedPreferences onboardingPreferences(Activity activity) {
        return activity.getSharedPreferences(ONBOARDING_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    OnboardingPersister onboardingPersister(SharedPreferences preferences) {
        return new OnboardingPersister(preferences);
    }

    @Provides
    public Onboarding onboarding(OnboardingPersister persister) {
        return new Onboarding(persister);
    }
}
