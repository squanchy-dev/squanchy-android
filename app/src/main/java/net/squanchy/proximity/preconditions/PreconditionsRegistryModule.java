package net.squanchy.proximity.preconditions;

import android.app.Activity;
import android.bluetooth.BluetoothManager;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;
import java.util.List;

import net.squanchy.injection.ActivityContextModule;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.BLUETOOTH_SERVICE;

@Module(includes = {ActivityContextModule.class, OptInPreferencePersisterModule.class})
public class PreconditionsRegistryModule {

    private final GoogleApiClient googleApiClient;

    public PreconditionsRegistryModule(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    @Provides
    BluetoothManager bluetoothManager(Activity activity) {
        return (BluetoothManager) activity.getSystemService(BLUETOOTH_SERVICE);
    }

    @Provides
    LocationPermissionPrecondition locationPermissionPrecondition(Activity activity) {
        return new LocationPermissionPrecondition(activity);
    }

    @Provides
    LocationProviderPrecondition locationProviderPrecondition(Activity activity) {
        return new LocationProviderPrecondition(activity, googleApiClient);
    }

    @Provides
    BluetoothPrecondition bluetoothPrecondition(Activity activity, BluetoothManager bluetoothManager) {
        return new BluetoothPrecondition(activity, bluetoothManager);
    }

    @Provides
    OptInPrecondition optInPrecondition(ProximityOptInPersister proximityOptInPersister) {
        return new OptInPrecondition(proximityOptInPersister);
    }

    @Provides
    List<Precondition> preconditions(
            OptInPrecondition optInPrecondition,
            LocationPermissionPrecondition locationPermissionPrecondition,
            LocationProviderPrecondition locationProviderPrecondition,
            BluetoothPrecondition bluetoothPrecondition
    ) {
        return Arrays.asList(
                optInPrecondition,
                locationPermissionPrecondition,
                locationProviderPrecondition,
                bluetoothPrecondition
        );
    }

    @Provides
    PreconditionsRegistry preconditionsRegistry(List<Precondition> preconditions) {
        return new PreconditionsRegistry(preconditions);
    }
}
