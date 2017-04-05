package net.squanchy.support.debug;

import android.content.Context;
import android.content.SharedPreferences;

public class OnboardingResetter {

    private final SharedPreferences preferences;

    public OnboardingResetter(Context context) {
        preferences = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE);
    }

    public void resetOnboarding() {
        preferences.edit()
                .clear()
                .apply();
    }
}
