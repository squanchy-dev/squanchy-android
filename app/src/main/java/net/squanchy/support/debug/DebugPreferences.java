package net.squanchy.support.debug;

import android.content.Context;
import android.content.SharedPreferences;

import net.squanchy.BuildConfig;

public class DebugPreferences {

    private static final String PREFERENCES_NAME_DEBUG = "debug";
    private static final String KEY_CONTEST_TESTING_ENABLED = "contest.testing_enabled";
    private static final boolean IS_DEBUG = BuildConfig.DEBUG;

    private final SharedPreferences preferences;

    DebugPreferences(Context context) {
        this.preferences = context.getSharedPreferences(DebugPreferences.PREFERENCES_NAME_DEBUG, Context.MODE_PRIVATE);
    }

    public boolean contestTestingEnabled() {
        return IS_DEBUG && preferences.getBoolean(KEY_CONTEST_TESTING_ENABLED, false);
    }

    void enableContestTesting() {
        storeDebugPreference(KEY_CONTEST_TESTING_ENABLED, true);
    }

    void disableContestTesting() {
        storeDebugPreference(KEY_CONTEST_TESTING_ENABLED, false);
    }

    private void storeDebugPreference(String key, boolean value) {
        if (!IS_DEBUG) {
            return;
        }
        preferences.edit()
                .putBoolean(key, value)
                .apply();
    }
}
