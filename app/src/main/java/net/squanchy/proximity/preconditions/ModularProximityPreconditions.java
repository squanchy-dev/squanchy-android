package net.squanchy.proximity.preconditions;

import android.content.Intent;
import android.support.annotation.MainThread;

import net.squanchy.proximity.preconditions.LocationProviderPrecondition.ProviderPreconditionException;
import net.squanchy.support.lang.Optional;

import timber.log.Timber;

public class ModularProximityPreconditions implements ProximityPreconditions {

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
        Timber.i(requestingPrecondition.toString());
        if (requestingPrecondition.isPresent()) {
            startCheckingFrom(requestingPrecondition.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Optional<Precondition> requestingPrecondition = registry.findPreconditionHandlingRequestCode(requestCode);
        if (requestingPrecondition.isPresent()) {
            startCheckingFrom(requestingPrecondition.get());
            return true;
        } else {
            return false;
        }
    }

    private void startCheckingFrom(Precondition precondition) {
        if (!precondition.available()) {
            Timber.d("Skipping unavailable precondition: %s", precondition);
            return;
        }

        if (precondition.satisfied()) {
            continueAfterSucceedingCheck(precondition);
        } else {
            precondition.satisfy()
                    .subscribe(() -> {
                        if (precondition.satisfied()) {
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
