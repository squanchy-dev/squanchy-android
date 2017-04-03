package net.squanchy.onboarding;

import net.squanchy.proximity.preconditions.ProximityPreconditions;
import net.squanchy.support.lang.Optional;

public class Onboarding {

    private final OnboardingPersister persister;
    private final ProximityPreconditions proximityPreconditions;

    Onboarding(OnboardingPersister persister, ProximityPreconditions proximityPreconditions) {
        this.persister = persister;
        this.proximityPreconditions = proximityPreconditions;
    }

    public Optional<OnboardingPage> nextPageToShow() {
        for (OnboardingPage onboardingPage : OnboardingPage.values()) {
            if (shouldShowNext(onboardingPage)) {
                return Optional.of(onboardingPage);
            }
        }
        return Optional.absent();
    }

    private boolean shouldShowNext(OnboardingPage page) {
        boolean pageNotSeenYet = !persister.pageSeen(page);
        boolean needToShowPage = needToShow(page);
        return pageNotSeenYet && needToShowPage;
    }

    private boolean needToShow(OnboardingPage page) {
        switch (page) {
            case LOCATION:
                return proximityPreconditions.isProximityAvailable() && proximityPreconditions.needsActionToSatisfyPreconditions();
            default:
                return true;
        }
    }

    public void savePageSeen(OnboardingPage page) {
        persister.savePageSeen(page);
    }
}
