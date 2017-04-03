package net.squanchy.onboarding;

import net.squanchy.remoteconfig.RemoteConfig;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class Onboarding {

    private final OnboardingPersister persister;
    private final RemoteConfig remoteConfig;

    Onboarding(OnboardingPersister persister, RemoteConfig remoteConfig) {
        this.persister = persister;
        this.remoteConfig = remoteConfig;
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
                return remoteConfig.proximityServicesEnabled();
            default:
                return Single.just(true);
        }
    }

    public void savePageSeen(OnboardingPage page) {
        persister.savePageSeen(page);
    }
}
