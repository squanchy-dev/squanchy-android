package net.squanchy.onboarding;

import android.app.Activity;

@SuppressWarnings("NoWhitespaceBefore")     // TODO we need to fill in the pages for the onboarding
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
