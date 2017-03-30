package net.squanchy.onboarding;

import android.app.Activity;

import net.squanchy.onboarding.location.LocationOnboardingActivity;

public enum OnboardingPage {
    LOCATION(LocationOnboardingActivity.class);                           // TODO create activity

    private final Class<? extends Activity> activityClass;

    OnboardingPage(Class<? extends Activity> activityClass) {
        this.activityClass = activityClass;
    }

    public Class<? extends Activity> activityClass() {
        return activityClass;
    }
}
