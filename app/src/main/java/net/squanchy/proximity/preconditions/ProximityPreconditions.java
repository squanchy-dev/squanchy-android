package net.squanchy.proximity.preconditions;

import android.content.Intent;

public interface ProximityPreconditions {

    boolean needsActionToSatisfyPreconditions();

    void startSatisfyingPreconditions();

    @SuppressWarnings("PMD.UseVarargs") // This array is passed to the Android APIs and is kept for consistency
    boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    boolean onActivityResult(int requestCode, int resultCode, Intent data);

    interface Callback {

        void notOptedIn();

        void permissionDenied();

        void locationProviderDenied();

        void bluetoothDenied();

        void allChecksPassed();

        void exceptionWhileSatisfying(Throwable throwable);

        void recheckAfterActivityResult();
    }
}
