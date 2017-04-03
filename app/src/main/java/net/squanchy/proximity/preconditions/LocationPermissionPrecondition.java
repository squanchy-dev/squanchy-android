package net.squanchy.proximity.preconditions;

import android.Manifest;

import net.squanchy.support.lang.Optional;

import io.reactivex.Single;

class LocationPermissionPrecondition implements Precondition {

    private static final int REQUEST_GRANT_PERMISSIONS = 9878;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    private final TaskLauncher taskLauncher;

    LocationPermissionPrecondition(TaskLauncher taskLauncher) {
        this.taskLauncher = taskLauncher;
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
        return taskLauncher.permissionGranted(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public Single<SatisfyResult> satisfy() {
        return Single.create(emitter -> {
            taskLauncher.requestPermissions(REQUIRED_PERMISSIONS, REQUEST_GRANT_PERMISSIONS);
            emitter.onSuccess(SatisfyResult.WAIT_FOR_EXTERNAL_RESULT);
        });
    }

    @Override
    public Optional<Integer> requestCode() {
        return Optional.of(REQUEST_GRANT_PERMISSIONS);
    }
}
