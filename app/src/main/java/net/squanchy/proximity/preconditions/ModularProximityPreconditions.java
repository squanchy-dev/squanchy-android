package net.squanchy.proximity.preconditions;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.MainThread;

import net.squanchy.support.lang.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import timber.log.Timber;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class ModularProximityPreconditions implements ProximityPreconditions {

    private static final int REQUEST_LOCATION_SETTINGS = 2541;
    private static final int RESULT_ABORT = Activity.RESULT_FIRST_USER;

    private final PreconditionsRegistry registry;
    private final Callback callback;

    ModularProximityPreconditions(PreconditionsRegistry registry, Callback callback) {
        this.registry = registry;
        this.callback = callback;
    }

    @Override
    public boolean needsActionToSatisfyPreconditions() {
        return registry.anyUnsatisfied();
    }

    @Override
    @MainThread
    public void startSatisfyingPreconditions() {
        Precondition precondition = registry.firstPrecondition();
        startCheckingFrom(precondition);
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Optional<Precondition> requestingPrecondition = registry.findPreconditionHandlingRequestCode(requestCode);
        if (requestingPrecondition.isPresent()) {
            handlePermissionsRequestResult(requestingPrecondition.get(), grantResults);
            return true;
        } else {
            return false;
        }
    }

    private void handlePermissionsRequestResult(Precondition precondition, int[] grantResults) {
        if (deniedAnyPermissions(grantResults)) {
            callback.permissionDenied();
        } else {
            startCheckingFrom(precondition);
        }
    }

    private boolean deniedAnyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PERMISSION_DENIED) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION_SETTINGS) {
            callback.recheckAfterActivityResult();
            return true;
        }

        Optional<Precondition> requestingPrecondition = registry.findPreconditionHandlingRequestCode(requestCode);
        if (requestingPrecondition.isPresent()) {
            handleCheckResult(requestingPrecondition.get(), resultCode);
            return true;
        } else {
            return false;
        }
    }

    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED") // Cannot handle the subscription here
    private void startCheckingFrom(Precondition precondition) {
        if (precondition.satisfied()) {
            continueAfterSucceedingCheck(precondition);
        } else {
            precondition.satisfy()
                    .subscribe(result -> {
                        if (result == Precondition.SatisfyResult.ABORT) {
                            handleCheckResult(precondition, RESULT_ABORT);
                        } else if (result == Precondition.SatisfyResult.SUCCESS) {
                            continueAfterSucceedingCheck(precondition);
                        } else if (result == Precondition.SatisfyResult.WAIT_FOR_EXTERNAL_RESULT) {
                            // Waiting on some external resource (e.g., onActivityResult) that will
                            // unblock us in the onActivityResult/onPermissionRequestResult. We give
                            // up for now, those will restart us.
                            Timber.d("Precondition requires us to check again: %s", precondition.getClass().getSimpleName());
                        }
                    }, this::handleCheckError);
        }
    }

    private void handleCheckResult(Precondition precondition, int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            startCheckingFrom(precondition);
        } else if (precondition instanceof BluetoothPrecondition) {
            callback.bluetoothDenied();
        } else if (precondition instanceof LocationProviderPrecondition) {
            callback.locationProviderDenied();
        } else if (precondition instanceof OptInPrecondition) {
            callback.notOptedIn();
        }
    }

    private void continueAfterSucceedingCheck(Precondition passedPrecondition) {
        Optional<Precondition> nextPrecondition = registry.preconditionAfter(passedPrecondition);
        if (nextPrecondition.isPresent()) {
            startCheckingFrom(nextPrecondition.get());
        } else {
            callback.allChecksPassed();
        }
    }

    private void handleCheckError(Throwable throwable) {
        callback.exceptionWhileSatisfying(throwable);
    }
}
