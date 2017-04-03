package net.squanchy.proximity.preconditions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import net.squanchy.support.lang.Optional;

import io.reactivex.Single;

class LocationPermissionPrecondition implements Precondition {

    private static final int REQUEST_GRANT_PERMISSIONS = 9878;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    private final Activity activity;

    LocationPermissionPrecondition(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean available() {
        return ALWAYS_AVAILABLE;
    }

    @Override
    public boolean performsSynchronousSatisfiedCheck() {
        return CAN_PERFORM_SYNCHRONOUS_CHECK;
    }

    @Override
    public boolean satisfied() {
        int granted = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        return granted == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public Single<SatisfyResult> satisfy() {
        return Single.create(emitter -> {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, REQUEST_GRANT_PERMISSIONS);
            emitter.onSuccess(SatisfyResult.RETRY);
        });
    }

    @Override
    public Optional<Integer> requestCode() {
        return Optional.of(REQUEST_GRANT_PERMISSIONS);
    }
}
