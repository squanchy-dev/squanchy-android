package net.squanchy.contest;

import net.squanchy.injection.ApplicationInjector;
import net.squanchy.support.injection.CurrentTimeModule;

public class ContestInjector {

    private ContestInjector() {
    }

    public static ContestComponent obtain(ContestActivity activity) {
        return DaggerContestComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .currentTimeModule(new CurrentTimeModule())
                .contestModule(new ContestModule())
                .build();
    }
}
