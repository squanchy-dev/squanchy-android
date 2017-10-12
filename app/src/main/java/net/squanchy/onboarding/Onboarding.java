package net.squanchy.onboarding;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class Onboarding {

    private final OnboardingPersister persister;

    Onboarding(OnboardingPersister persister) {
        this.persister = persister;
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
        return Single.just(true);
    }

    public void savePageSeen(OnboardingPage page) {
        persister.savePageSeen(page);
    }
}
