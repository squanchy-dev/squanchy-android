package net.squanchy.proximity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class ProximityPreconditions {

    private static final int REQUEST_GRANT_PERMISSIONS = 9878;
    private static final int REQUEST_ENABLE_BLUETOOTH = 8909;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    private final Activity activity;
    private final BluetoothManager bluetoothManager;
    private final Callback callback;

    public ProximityPreconditions(Activity activity, BluetoothManager bluetoothManager, Callback callback) {
        this.activity = activity;
        this.bluetoothManager = bluetoothManager;
        this.callback = callback;
    }

    public void startOptInProcedure() {
        if (hasLocationPermission()) {
            requestBluetoothIfCurrentlyDisabled();
        } else {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, REQUEST_GRANT_PERMISSIONS
            );
        }
    }

    private boolean hasLocationPermission() {
        int granted = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        return granted == PackageManager.PERMISSION_GRANTED;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_GRANT_PERMISSIONS) {
            handlePermissionRequestResult(grantResults);
        } else {
            callback.bubbleUpOnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void handlePermissionRequestResult(int[] grantResults) {
        if (hasGrantedFineLocationAccess(grantResults)) {
            requestBluetoothIfCurrentlyDisabled();
        } else {
            callback.permissionDenied();
        }
    }

    private boolean hasGrantedFineLocationAccess(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            handleBluetoothRequestResult(resultCode);
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

    public interface Callback {

        void bubbleUpOnActivityResult(int requestCode, int resultCode, Intent data);

        void bubbleUpOnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

        void allChecksPassed();

        void permissionDenied();

        void bluetoothDenied();
    }
}
