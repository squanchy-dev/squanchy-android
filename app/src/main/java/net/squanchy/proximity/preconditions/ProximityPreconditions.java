package net.squanchy.proximity.preconditions;

import android.content.Intent;

public interface ProximityPreconditions {

    boolean needsActionToSatisfyPreconditions();

    void startSatisfyingPreconditions();

    boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    boolean onActivityResult(int requestCode, int resultCode, Intent data);

    interface Callback {

        void allChecksPassed();

        void permissionDenied();

        void locationProviderFailed(LocationProviderPrecondition.FailureInfo failureStatus);

        void bluetoothDenied();

        void exceptionWhileSatisfying(Throwable throwable);
    }
}
