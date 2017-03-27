package net.squanchy.home.deeplink;

import android.content.Intent;
import android.os.Bundle;

import net.squanchy.home.BottomNavigationSection;
import net.squanchy.home.HomeStatePersister;
import net.squanchy.support.lang.Optional;

import static net.squanchy.home.deeplink.HomeActivityDeepLinkCreator.KEY_INITIAL_PAGE_INDEX;

public class HomeActivityIntentParser {

    private final HomeStatePersister statePersister;
    private final Optional<Bundle> savedState;
    private final Intent intent;

    public HomeActivityIntentParser(Optional<Bundle> savedState, Intent intent) {
        this.statePersister = new HomeStatePersister();
        this.savedState = savedState;
        this.intent = intent;
    }

    public BottomNavigationSection getInitialSelectedPage() {
        return getSectionFrom(statePersister, savedState)
                .or(getSectionFrom(Optional.fromNullable(intent.getExtras())))
                .or(BottomNavigationSection.SCHEDULE);
    }

    private Optional<BottomNavigationSection> getSectionFrom(HomeStatePersister statePersister, Optional<Bundle> savedState) {
        return statePersister.readSectionFrom(savedState);
    }

    private Optional<BottomNavigationSection> getSectionFrom(Optional<Bundle> intentExtras) {
        Bundle bundle = intentExtras.or(Bundle.EMPTY);

        if (bundle.containsKey(KEY_INITIAL_PAGE_INDEX)) {
            int sectionIndex = bundle.getInt(KEY_INITIAL_PAGE_INDEX);
            return Optional.of(BottomNavigationSection.values()[sectionIndex]);
        } else {
            return Optional.absent();
        }
    }
}
