package net.squanchy.proximity.preconditions;

import android.content.Intent;

public interface ProximityPreconditions {

    void startSatisfyingPreconditions();

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    interface Callback {

        void bubbleUpOnActivityResult(int requestCode, int resultCode, Intent data);

        void bubbleUpOnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

        void allChecksPassed();

        void permissionDenied();

        void locationProviderFailed(LocationProviderPrecondition.FailureInfo failureStatus);

        void bluetoothDenied();

        void exceptionWhileSatisfying(Throwable throwable);
    }
}
