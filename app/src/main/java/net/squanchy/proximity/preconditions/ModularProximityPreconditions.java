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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Optional<Precondition> requestingPrecondition = registry.findPreconditionHandlingRequestCode(requestCode);
        Timber.i(requestingPrecondition.toString());
        if (requestingPrecondition.isPresent()) {
            startCheckingFrom(requestingPrecondition.get());
        } else {
            callback.bubbleUpOnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Optional<Precondition> requestingPrecondition = registry.findPreconditionHandlingRequestCode(requestCode);
        if (requestingPrecondition.isPresent()) {
            startCheckingFrom(requestingPrecondition.get());
        } else {
            callback.bubbleUpOnActivityResult(requestCode, resultCode, data);
        }
    }

    private void startCheckingFrom(Precondition initialPrecondition) {
        Optional<Precondition> nextPrecondition = Optional.of(initialPrecondition);
        while (nextPrecondition.isPresent()) {
            Precondition precondition = nextPrecondition.get();
            if (!precondition.available()) {
                Timber.d("Skipping unavailable precondition: %s", precondition);
                return;
            }

            if (precondition.satisfied()) {
                nextPrecondition = registry.preconditionAfter(precondition);
            } else {
                precondition.satisfy()
                        .subscribe(() -> {
                            if (!precondition.satisfied()) {
                                // Waiting on some external resource (e.g., onActivityResult) that will
                                // unblock us in the onActivityResult/onPermissionRequestResult. We give
                                // up for now, those will restart us.
                                return;
                            }

                            Optional<Precondition> preconditionOptional = registry.preconditionAfter(precondition);
                            if (preconditionOptional.isPresent()) {
                                startCheckingFrom(preconditionOptional.get());
                            } else {
                                callback.allChecksPassed();
                            }
                        });
                return;
            }
        }

        callback.allChecksPassed();
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
