package net.squanchy.onboarding;

import android.content.SharedPreferences;

import java.util.Locale;

public class OnboardingPersister {

    private final SharedPreferences preferences;

    public OnboardingPersister(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    void savePageSeen(OnboardingPage page) {
        preferences.edit()
                .putBoolean(preferenceKeyFor(page), true)
                .apply();
    }

    boolean pageSeen(OnboardingPage page) {
        return preferences.getBoolean(preferenceKeyFor(page), false);
    }

    private String preferenceKeyFor(OnboardingPage page) {
        return String.format("key_onboarding_%s_seen", page.name().toLowerCase(Locale.US));
    }
}
