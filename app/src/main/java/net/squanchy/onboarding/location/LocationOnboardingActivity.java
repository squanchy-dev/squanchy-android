package net.squanchy.onboarding.location;

import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.onboarding.Onboarding;
import net.squanchy.onboarding.OnboardingPage;
import net.squanchy.proximity.ProximityPreconditions;
import net.squanchy.service.proximity.injection.ProximityService;

public class LocationOnboardingActivity extends TypefaceStyleableActivity {

    private ProximityPreconditions proximityPreconditions;
    private Onboarding onboarding;
    private ProximityService service;

    private View contentRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationOnboardingComponent component = LocationOnboardingInjector.obtain(this);
        onboarding = component.onboarding();
        service = component.proximityService();

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        proximityPreconditions = new ProximityPreconditions(this, bluetoothManager, proximityPreconditionsCallback());

        setContentView(R.layout.activity_location_onboarding);
        contentRoot = findViewById(R.id.onboarding_content_root);

        findViewById(R.id.skip_button).setOnClickListener(view -> markPageAsSeenAndFinish());
        findViewById(R.id.location_opt_in_button).setOnClickListener(view -> proximityPreconditions.startOptInProcedure());

        setResult(RESULT_CANCELED);
    }

    private ProximityPreconditions.Callback proximityPreconditionsCallback() {
        return new ProximityPreconditions.Callback() {
            @Override
            public void bubbleUpOnActivityResult(int requestCode, int resultCode, Intent data) {
                LocationOnboardingActivity.super.onActivityResult(requestCode, resultCode, data);
            }

            @Override
            public void bubbleUpOnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                LocationOnboardingActivity.super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }

            @Override
            public void allChecksPassed() {
                startRadarAndFinish();
            }

            @Override
            public void permissionDenied() {
                Snackbar.make(contentRoot, R.string.onboarding_error_permission_denied, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void bluetoothDenied() {
                Snackbar.make(contentRoot, R.string.onboarding_error_bluetooth_denied, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void startRadarAndFinish() {
        service.startRadar();
        markPageAsSeenAndFinish();
    }

    void markPageAsSeenAndFinish() {
        onboarding.savePageSeen(OnboardingPage.LOCATION);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        proximityPreconditions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        proximityPreconditions.onActivityResult(requestCode, resultCode, data);
    }
}
