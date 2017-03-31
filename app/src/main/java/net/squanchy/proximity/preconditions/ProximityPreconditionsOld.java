package net.squanchy.proximity.preconditions;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import timber.log.Timber;

public class ProximityPreconditionsOld implements ProximityPreconditions {

    private static final int REQUEST_GRANT_PERMISSIONS = 9878;
    private static final int REQUEST_ENABLE_BLUETOOTH = 8909;
    private static final int REQUEST_ENABLE_LOCATION_PROVIDER = 8999;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    private final Activity activity;
    private final BluetoothManager bluetoothManager;
    private final GoogleApiClient googleApiClient;
    private final Callback callback;

    public ProximityPreconditionsOld(Activity activity, BluetoothManager bluetoothManager, GoogleApiClient googleApiClient, Callback callback) {
        this.activity = activity;
        this.bluetoothManager = bluetoothManager;
        this.googleApiClient = googleApiClient;
        this.callback = callback;
    }

    @Override
    public boolean needsActionToSatisfyPreconditions() {
        return true;       // This will have us re-check all preconditions every time; we don't know how to pre-check unfortunately
    }

    @Override
    public void startSatisfyingPreconditions() {
        if (hasLocationPermission()) {
            enableLocationProviderIfNecessaryThenProceed();
        } else {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, REQUEST_GRANT_PERMISSIONS);
        }
    }

    private boolean hasLocationPermission() {
        int granted = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        return granted == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_GRANT_PERMISSIONS) {
            handlePermissionRequestResult(grantResults);
        } else {
            callback.bubbleUpOnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void handlePermissionRequestResult(int[] grantResults) {
        if (hasGrantedFineLocationAccess(grantResults)) {
            enableLocationProviderIfNecessaryThenProceed();
        } else {
            callback.permissionDenied();
        }
    }

    private boolean hasGrantedFineLocationAccess(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    private void enableLocationProviderIfNecessaryThenProceed() {
        LocationSettingsRequest settingsRequest = createLocationSettingsRequest();
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, settingsRequest);

        result.setResultCallback(locationSettingsResult -> {
            Status status = locationSettingsResult.getStatus();
            LocationSettingsStates locationSettingsStates = locationSettingsResult.getLocationSettingsStates();

            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    requestBluetoothIfCurrentlyDisabled();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(activity, REQUEST_ENABLE_LOCATION_PROVIDER);
                    } catch (IntentSender.SendIntentException e) {
                        Timber.e(e);
                        callback.locationProviderFailed(LocationProviderPrecondition.FailureInfo.from(locationSettingsStates));
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    callback.locationProviderFailed(LocationProviderPrecondition.FailureInfo.from(locationSettingsStates));
                    break;
            }
        });
    }

    private LocationSettingsRequest createLocationSettingsRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();
    }

    private void requestBluetoothIfCurrentlyDisabled() {
        BluetoothAdapter adapter = bluetoothManager.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("This device doesn't seem to have a Bluetooth radio, how are we even getting here?");
        } else if (!adapter.isEnabled()) {
            requestBluetooth();
        } else {
            callback.allChecksPassed();
        }
    }

    private void requestBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            handleBluetoothRequestResult(resultCode);
        } else if (requestCode == REQUEST_ENABLE_LOCATION_PROVIDER) {
            requestBluetoothIfCurrentlyDisabled();
        } else {
            callback.bubbleUpOnActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleBluetoothRequestResult(int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            callback.allChecksPassed();
        } else {
            callback.bluetoothDenied();
        }
    }
}
