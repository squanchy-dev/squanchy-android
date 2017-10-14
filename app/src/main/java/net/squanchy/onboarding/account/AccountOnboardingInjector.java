package net.squanchy.onboarding.account;

import android.support.v7.app.AppCompatActivity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;

final class AccountOnboardingInjector {

    private AccountOnboardingInjector() {
        // no instances
    }

    public static AccountOnboardingComponent obtain(AppCompatActivity activity) {
        return DaggerAccountOnboardingComponent.builder()
                .activityContextModule(new ActivityContextModule(activity))
                .applicationComponent(ApplicationInjector.obtain(activity))
                .build();
    }
}
