package net.squanchy.proximity;

import android.bluetooth.BluetoothManager;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.remoteconfig.RemoteConfig;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityContextModule.class, BluetoothModule.class})
public class ProximityFeatureModule {

    @Provides
    ProximityFeature proximityFeature(RemoteConfig remoteConfig, BluetoothManager bluetoothManager) {
        return new ProximityFeature(remoteConfig, bluetoothManager);
    }
}
