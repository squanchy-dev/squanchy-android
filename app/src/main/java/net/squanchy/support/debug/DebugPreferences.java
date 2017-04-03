package net.squanchy.support.debug;

import android.content.Context;
import android.content.SharedPreferences;

public class DebugPreferences {

    private static final String PREFERENCES_NAME_DEBUG = "debug";
    private static final String KEY_CONTEST_TESTING_ENABLED = "contest.testing_enabled";

    private final SharedPreferences preferences;

    public DebugPreferences(Context context) {
        this.preferences = context.getSharedPreferences(DebugPreferences.PREFERENCES_NAME_DEBUG, Context.MODE_PRIVATE);
    }

    public boolean contestTestingEnabled() {
        return preferences.getBoolean(KEY_CONTEST_TESTING_ENABLED, false);
    }

    public void enableContestTesting() {
        storePreference(KEY_CONTEST_TESTING_ENABLED, true);
    }

    public void disableContestTesting() {
        storePreference(KEY_CONTEST_TESTING_ENABLED, false);
    }

    private void storePreference(String key, boolean value) {
        preferences.edit()
                .putBoolean(key, value)
                .apply();
    }
}
