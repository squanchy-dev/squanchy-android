package net.squanchy.proximity;

import android.bluetooth.BluetoothManager;

import net.squanchy.remoteconfig.RemoteConfig;

import io.reactivex.Single;

public class ProximityFeature {

    private final RemoteConfig remoteConfig;
    private final BluetoothManager bluetoothManager;

    public ProximityFeature(RemoteConfig remoteConfig, BluetoothManager bluetoothManager) {
        this.remoteConfig = remoteConfig;
        this.bluetoothManager = bluetoothManager;
    }

    public Single<Boolean> enabled() {
        Single<Boolean> proximityEnabled = remoteConfig.proximityServicesEnabled();
        Single<Boolean> bluetoothAvailable = Single.just(bluetoothAvailable());
        return Single.zip(
                proximityEnabled,
                bluetoothAvailable,
                (proximity, bluetooth) -> proximity && bluetooth
        );
    }

    private boolean bluetoothAvailable() {
        return bluetoothManager.getAdapter() != null;
    }
}
