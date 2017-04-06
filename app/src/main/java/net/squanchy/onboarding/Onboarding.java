package net.squanchy.onboarding;

import net.squanchy.proximity.ProximityFeature;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class Onboarding {

    private final OnboardingPersister persister;
    private final ProximityFeature proximityFeature;

    Onboarding(OnboardingPersister persister, ProximityFeature proximityFeature) {
        this.persister = persister;
        this.proximityFeature = proximityFeature;
    }

    public Maybe<OnboardingPage> nextPageToShow() {
        return Observable.fromArray(OnboardingPage.values())
                .flatMapMaybe(page -> canShow(page)
                        .filter(canShow -> canShow)
                        .map(canShow -> page)
                )
                .firstElement();
    }

    private Single<Boolean> canShow(OnboardingPage page) {
        if (persister.pageSeen(page)) {
            return Single.just(false);
        }

        switch (page) {
            case LOCATION:
                return proximityFeature.enabled();
            default:
                return Single.just(true);
        }
    }

    public void savePageSeen(OnboardingPage page) {
        persister.savePageSeen(page);
    }
}
