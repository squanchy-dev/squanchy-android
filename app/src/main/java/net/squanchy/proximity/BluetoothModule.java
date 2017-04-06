package net.squanchy.proximity;

import android.app.Application;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class BluetoothModule {

    @Provides
    BluetoothManager bluetoothManager(Application application) {
        return (BluetoothManager) application.getSystemService(Context.BLUETOOTH_SERVICE);
    }
}
