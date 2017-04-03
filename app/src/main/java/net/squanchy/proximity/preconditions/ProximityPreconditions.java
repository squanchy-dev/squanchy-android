package net.squanchy.proximity.preconditions;

import android.content.Intent;

public interface ProximityPreconditions {

    boolean isProximityAvailable();

    boolean needsActionToSatisfyPreconditions();

    void startSatisfyingPreconditions();

    boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    boolean onActivityResult(int requestCode, int resultCode, Intent data);

    interface Callback {

        void notOptedIn();

        void featureDisabled();

        void permissionDenied();

        void locationProviderDenied();

        void bluetoothDenied();

        void allChecksPassed();

        void exceptionWhileSatisfying(Throwable throwable);

        void recheckAfterActivityResult();
    }
}
