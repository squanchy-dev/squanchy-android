package net.squanchy.onboarding;

import android.app.Activity;

import net.squanchy.onboarding.account.AccountOnboardingActivity;

public enum OnboardingPage {
    ACCOUNT(AccountOnboardingActivity.class);

    private final Class<? extends Activity> activityClass;

    OnboardingPage(Class<? extends Activity> activityClass) {
        this.activityClass = activityClass;
    }

    public Class<? extends Activity> activityClass() {
        return activityClass;
    }
}
