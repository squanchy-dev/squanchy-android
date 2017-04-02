package net.squanchy.proximity.preconditions;

import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;
import java.util.List;

import net.squanchy.injection.ActivityContextModule;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.BLUETOOTH_SERVICE;

@Module(includes = ActivityContextModule.class)
public class PreconditionsRegistryModule {

    private static final String OPT_IN_PREFERENCES_NAME = "opt_in_preferences";
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
    OptInPreferencePersister optInPreferencePersister(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(OPT_IN_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new OptInPreferencePersister(preferences);
    }

    @Provides
    OptInPrecondition optInPrecondition(OptInPreferencePersister optInPreferencePersister) {
        return new OptInPrecondition(optInPreferencePersister);
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
