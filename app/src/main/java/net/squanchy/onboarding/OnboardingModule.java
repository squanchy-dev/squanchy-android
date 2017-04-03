package net.squanchy.onboarding;

import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.SharedPreferences;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.proximity.BluetoothModule;
import net.squanchy.proximity.ProximityFeature;
import net.squanchy.remoteconfig.RemoteConfig;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityContextModule.class, BluetoothModule.class})
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
    ProximityFeature proximityFeature(RemoteConfig remoteConfig, BluetoothManager bluetoothManager) {
        return new ProximityFeature(remoteConfig, bluetoothManager);
    }

    @Provides
    public Onboarding onboarding(OnboardingPersister persister, ProximityFeature proximityFeature) {
        return new Onboarding(persister, proximityFeature);
    }
}
