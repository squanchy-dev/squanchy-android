package net.squanchy.navigation;

import android.content.SharedPreferences;

class FirstStartPersister {

    private static final String KEY_HAS_BEEN_STARTED_ALREADY = "FirstStart.has_been_started_already";

    private final SharedPreferences preferences;

    FirstStartPersister(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    boolean hasBeenStartedAlready() {
        return preferences.getBoolean(KEY_HAS_BEEN_STARTED_ALREADY, false);
    }

    void storeHasBeenStarted() {
        preferences.edit()
                .putBoolean(KEY_HAS_BEEN_STARTED_ALREADY, true)
                .apply();
    }
}
