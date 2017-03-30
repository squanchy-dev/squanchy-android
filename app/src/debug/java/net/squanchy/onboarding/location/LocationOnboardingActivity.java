package net.squanchy.onboarding.location;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.onboarding.Onboarding;
import net.squanchy.onboarding.OnboardingPage;
import net.squanchy.service.proximity.injection.ProximityService;

public class LocationOnboardingActivity extends TypefaceStyleableActivity {

    private static final int REQUEST_GRANT_PERMISSIONS = 1000;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1001;

    private Onboarding onboarding;
    private ProximityService service;
    private BluetoothManager bluetoothService;

    private View contentRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationOnboardingComponent component = LocationOnboardingInjector.obtain(this);
        onboarding = component.onboarding();
        service = component.proximityService();

        bluetoothService = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);

        setContentView(R.layout.activity_location_onboarding);
        contentRoot = findViewById(R.id.onboarding_content_root);

        findViewById(R.id.skip_button).setOnClickListener(view -> markPageAsSeenAndFinish());
        findViewById(R.id.location_opt_in_button).setOnClickListener(view -> startOptInProcedure());

        setResult(RESULT_CANCELED);
    }

    private void startOptInProcedure() {
        if (hasLocationPermission()) {
            requestBluetoothIfCurrentlyDisabled();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_GRANT_PERMISSIONS
            );
        }
    }

    private boolean hasLocationPermission() {
        int granted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return granted == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_GRANT_PERMISSIONS) {
            handlePermissionRequestResult(grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void handlePermissionRequestResult(@NonNull int[] grantResults) {
        if (hasGrantedFineLocationAccess(grantResults)) {
            requestBluetoothIfCurrentlyDisabled();
        } else {
            Snackbar.make(contentRoot, R.string.onboarding_error_permission_denied, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasGrantedFineLocationAccess(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    private void requestBluetoothIfCurrentlyDisabled() {
        BluetoothAdapter adapter = bluetoothService.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("This device doesn't seem to have a Bluetooth radio, how are we even getting here?");
        } else if (!adapter.isEnabled()) {
            requestBluetooth();
        } else {
            startRadarAndFinish();
        }
    }

    private void requestBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            handleBluetoothRequestResult(resultCode);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleBluetoothRequestResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            startRadarAndFinish();
        } else {
            Snackbar.make(contentRoot, R.string.onboarding_error_bluetooth_denied, Toast.LENGTH_SHORT).show();
        }
    }

    private void startRadarAndFinish() {
        service.startRadar();   // TODO mark opt-in in ProximityFeature
        markPageAsSeenAndFinish();
    }

    void markPageAsSeenAndFinish() {
        onboarding.savePageSeen(OnboardingPage.LOCATION);
        setResult(RESULT_OK);
        finish();
    }
}
