package net.squanchy.contest;

import android.app.Activity;
import android.view.ViewGroup;

import net.squanchy.support.debug.DebugPreferences;

@SuppressWarnings("unused")    // This is a no-op version for a debug facility
class ContestTester {

    ContestTester(Activity activity, DebugPreferences debugPreferences) {
        // No-op (only does stuff in debug)
    }

    boolean testingEnabled() {
        return false;
    }

    void appendDebugControls(ViewGroup container, ContestService contestService) {
        // No-op (only does stuff in debug)
    }
}
