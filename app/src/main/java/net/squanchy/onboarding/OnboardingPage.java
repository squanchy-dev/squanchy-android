package net.squanchy.onboarding;

import android.app.Activity;

public enum OnboardingPage {
    ;

    private final Class<? extends Activity> activityClass;

    OnboardingPage(Class<? extends Activity> activityClass) {
        this.activityClass = activityClass;
    }

    public Class<? extends Activity> activityClass() {
        return activityClass;
    }
}
