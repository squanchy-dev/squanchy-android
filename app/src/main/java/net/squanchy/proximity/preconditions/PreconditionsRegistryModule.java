package net.squanchy.proximity.preconditions;

import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.location.LocationManager;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;
import java.util.List;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.proximity.BluetoothModule;
import net.squanchy.support.debug.DebugPreferencesModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityContextModule.class, OptInPreferencePersisterModule.class, BluetoothModule.class, DebugPreferencesModule.class})
public class PreconditionsRegistryModule {

    private final GoogleApiClient googleApiClient;
    private final TaskLauncher taskLauncher;

    public PreconditionsRegistryModule(GoogleApiClient googleApiClient, TaskLauncher taskLauncher) {
        this.googleApiClient = googleApiClient;
        this.taskLauncher = taskLauncher;
    }

    @Provides
    OptInPrecondition optInPrecondition(ProximityOptInPersister proximityOptInPersister) {
        return new OptInPrecondition(proximityOptInPersister);
    }

    @Provides
    LocationPermissionPrecondition locationPermissionPrecondition() {
        return new LocationPermissionPrecondition(taskLauncher);
    }

    @Provides
    LocationManager locationManager(Activity activity) {
        return (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    @Provides
    LocationProviderPrecondition locationProviderPrecondition(LocationManager locationManager) {
        return new LocationProviderPrecondition(taskLauncher, googleApiClient, locationManager);
    }

    @Provides
    BluetoothPrecondition bluetoothPrecondition(BluetoothManager bluetoothManager) {
        return new BluetoothPrecondition(bluetoothManager, taskLauncher);
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
