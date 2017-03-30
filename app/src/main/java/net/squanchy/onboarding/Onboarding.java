package net.squanchy.onboarding;

import net.squanchy.support.lang.Optional;

public class Onboarding {

    private final OnboardingPersister persister;

    Onboarding(OnboardingPersister persister) {
        this.persister = persister;
    }

    public Optional<OnboardingPage> nextPageToShow() {
        for (OnboardingPage onboardingPage : OnboardingPage.values()) {
            if (!persister.pageSeen(onboardingPage)) {
                return Optional.of(onboardingPage);
            }
        }
        return Optional.absent();
    }

    public void savePageSeen(OnboardingPage page) {
        persister.savePageSeen(page);
    }
}
