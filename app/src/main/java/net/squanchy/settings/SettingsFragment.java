package net.squanchy.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import net.squanchy.BuildConfig;
import net.squanchy.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preferences);
        displayBuildVersion();
    }

    private void displayBuildVersion() {
        String buildVersionKey = getString(R.string.buid_version_preference_key);
        Preference buildVersionPreference = findPreference(buildVersionKey);
        String buildVersion = String.format(getString(R.string.version_x), BuildConfig.VERSION_NAME);
        buildVersionPreference.setSummary(buildVersion);
    }
}
