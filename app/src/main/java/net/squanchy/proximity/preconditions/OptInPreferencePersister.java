package net.squanchy.proximity.preconditions;

import android.content.SharedPreferences;

public class OptInPreferencePersister {

    private static final String OPT_IN_PREFERENCE_KEY = "opt_in_preference";
    private final SharedPreferences preferences;

    public OptInPreferencePersister(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void setOptInPreferenceTo(boolean granted) {
        preferences.edit().putBoolean(OPT_IN_PREFERENCE_KEY, granted).apply();
    }

    public boolean getOptInPreferenceGranted() {
        return preferences.getBoolean(OPT_IN_PREFERENCE_KEY, true);
    }
}
