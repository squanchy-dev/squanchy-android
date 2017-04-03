package net.squanchy.proximity.preconditions;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.annotation.MainThread;

import net.squanchy.navigation.Navigator;
import net.squanchy.proximity.preconditions.LocationProviderPrecondition.ProviderPreconditionException;
import net.squanchy.support.lang.Optional;

import timber.log.Timber;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class ModularProximityPreconditions implements ProximityPreconditions {

    private static final int REQUEST_LOCATION_SETTINGS = 2541;

    private final PreconditionsRegistry registry;
    private final Navigator navigator;
    private final Callback callback;

    ModularProximityPreconditions(PreconditionsRegistry registry, Navigator navigator, Callback callback) {
        this.registry = registry;
        this.navigator = navigator;
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
            handleActivityResult(requestingPrecondition.get(), resultCode);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void navigateToLocationSettings() {
        try {
            navigator.toLocationSettingsForResult(REQUEST_LOCATION_SETTINGS);
        } catch (ActivityNotFoundException e) {
            Timber.e(e, "Unable to open location settings");
        }
    }

    private void handleActivityResult(Precondition precondition, int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            startCheckingFrom(precondition);
        } else if (precondition instanceof BluetoothPrecondition) {
            callback.bluetoothDenied();
        } else if (precondition instanceof LocationProviderPrecondition) {
            callback.locationProviderDenied();
        }
    }

    private void startCheckingFrom(Precondition precondition) {
        if (!precondition.available()) {
            Timber.d("Skipping unavailable precondition: %s", precondition);
            return;
        }

        boolean canCheckIfSatisfied = precondition.performsSynchronousSatisfiedCheck();
        if (canCheckIfSatisfied && precondition.satisfied()) {
            continueAfterSucceedingCheck(precondition);
        } else {
            precondition.satisfy()
                    .subscribe(() -> {
                        if (canCheckIfSatisfied && precondition.satisfied()) {
                            continueAfterSucceedingCheck(precondition);
                        }
                        // Waiting on some external resource (e.g., onActivityResult) that will
                        // unblock us in the onActivityResult/onPermissionRequestResult. We give
                        // up for now, those will restart us.
                    }, this::handleCheckError);
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
        if (throwable instanceof LocationProviderPrecondition.ProviderPreconditionException) {
            ProviderPreconditionException exception = (ProviderPreconditionException) throwable;
            callback.locationProviderFailed(exception.failureInfo());
        } else {
            callback.exceptionWhileSatisfying(throwable);
        }
    }
}
