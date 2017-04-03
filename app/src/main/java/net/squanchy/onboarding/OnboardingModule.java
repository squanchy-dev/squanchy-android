package net.squanchy.onboarding;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.proximity.preconditions.ProximityPreconditions;
import net.squanchy.proximity.preconditions.ProximityPreconditionsModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ProximityPreconditionsModule.class, ActivityContextModule.class})
public class OnboardingModule {

    private static final String ONBOARDING_PREFERENCES_NAME = "onboarding";

    @Provides
    OnboardingPersister onboardingPersister(Activity activity) {
        // The preferences cannot be @Provide'd because we need different ones in different places
        // (but they are all SharedPreferences) and the Persister is our abstraction of preferences.
        SharedPreferences preferences = activity.getSharedPreferences(ONBOARDING_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new OnboardingPersister(preferences);
    }

    @Provides
    public Onboarding onboarding(OnboardingPersister persister, ProximityPreconditions proximityPreconditions) {
        return new Onboarding(persister, proximityPreconditions);
    }
}
