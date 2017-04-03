package net.squanchy.proximity;

import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import net.squanchy.injection.ApplicationContextModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = ApplicationContextModule.class)
public class BluetoothModule {

    @Provides
    BluetoothManager bluetoothManager(Activity activity) {
        return (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
    }
}
