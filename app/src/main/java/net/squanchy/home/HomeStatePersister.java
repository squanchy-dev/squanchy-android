package net.squanchy.home;

import android.os.Bundle;

import net.squanchy.support.lang.Optional;

public class HomeStatePersister {

    private static final String STATE_KEY_SELECTED_PAGE_INDEX = "HomeActivity.selected_page_index";

    void saveCurrentSection(Bundle outState, BottomNavigationSection currentSection) {
        outState.putInt(STATE_KEY_SELECTED_PAGE_INDEX, currentSection.ordinal());
    }

    public Optional<BottomNavigationSection> readSectionFrom(Optional<Bundle> savedState) {
        Bundle bundle = savedState.or(Bundle.EMPTY);

        if (bundle.containsKey(STATE_KEY_SELECTED_PAGE_INDEX)) {
            int sectionIndex = bundle.getInt(STATE_KEY_SELECTED_PAGE_INDEX);
            return Optional.of(BottomNavigationSection.values()[sectionIndex]);
        } else {
            return Optional.absent();
        }
    }
}
