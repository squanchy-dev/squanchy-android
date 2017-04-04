package net.squanchy.proximity;

import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class BluetoothModule {

    @Provides
    BluetoothManager bluetoothManager(Activity activity) {
        return (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
    }
}
