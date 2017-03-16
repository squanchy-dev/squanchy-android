package net.squanchy.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.squanchy.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preferences);
    }
}
