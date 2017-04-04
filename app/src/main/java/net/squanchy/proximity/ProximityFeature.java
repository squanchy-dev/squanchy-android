package net.squanchy.proximity;

import android.bluetooth.BluetoothManager;

import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.support.debug.DebugPreferences;

import io.reactivex.Single;

public class ProximityFeature {

    private final RemoteConfig remoteConfig;
    private final BluetoothManager bluetoothManager;
    private final DebugPreferences debugPreferences;

    public ProximityFeature(RemoteConfig remoteConfig, BluetoothManager bluetoothManager, DebugPreferences debugPreferences) {
        this.remoteConfig = remoteConfig;
        this.bluetoothManager = bluetoothManager;
        this.debugPreferences = debugPreferences;
    }

    public Single<Boolean> enabled() {
        Single<Boolean> proximityEnabled = proximityEnabledFromRemoteConfig();
        Single<Boolean> bluetoothAvailable = Single.just(bluetoothAvailable());
        return Single.zip(
                proximityEnabled,
                bluetoothAvailable,
                (proximity, bluetooth) -> proximity && bluetooth
        );
    }

    private Single<Boolean> proximityEnabledFromRemoteConfig() {
        if (debugPreferences.contestTestingEnabled()) {
            return Single.just(true);
        }
        return remoteConfig.proximityServicesEnabled();
    }

    private boolean bluetoothAvailable() {
        return bluetoothManager.getAdapter() != null;
    }
}
