package net.squanchy.proximity.preconditions;

import android.content.SharedPreferences;

public class ProximityOptInPersister {

    private static final String OPT_IN_PREFERENCE_KEY = "opt_in_preference";
    private static final boolean DEFAULT_OPT_IN = true;

    private final SharedPreferences preferences;

    public ProximityOptInPersister(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void storeUserOptedIn() {
        setOptInPreferenceTo(true);
    }

    public void storeUserOptedOut() {
        setOptInPreferenceTo(false);
    }

    private void setOptInPreferenceTo(boolean granted) {
        preferences.edit().putBoolean(OPT_IN_PREFERENCE_KEY, granted).apply();
    }

    public boolean userOptedIn() {
        return preferences.getBoolean(OPT_IN_PREFERENCE_KEY, DEFAULT_OPT_IN);
    }
}
