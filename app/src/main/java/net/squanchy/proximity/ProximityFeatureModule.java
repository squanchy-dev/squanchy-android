package net.squanchy.proximity;

import android.bluetooth.BluetoothManager;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.support.debug.DebugPreferences;
import net.squanchy.support.debug.DebugPreferencesModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityContextModule.class, BluetoothModule.class, DebugPreferencesModule.class})
public class ProximityFeatureModule {

    @Provides
    ProximityFeature proximityFeature(RemoteConfig remoteConfig, BluetoothManager bluetoothManager, DebugPreferences debugPreferences) {
        return new ProximityFeature(remoteConfig, bluetoothManager, debugPreferences);
    }
}
