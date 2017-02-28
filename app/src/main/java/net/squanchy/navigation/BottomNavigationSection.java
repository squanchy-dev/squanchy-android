package net.squanchy.navigation;

import android.support.annotation.StyleRes;

import net.squanchy.R;

public enum BottomNavigationSection {
    SCHEDULE(R.style.Theme_Squanchy_Schedule),
    FAVORITES(R.style.Theme_Squanchy_Favorites),
    TWEETS(R.style.Theme_Squanchy_Tweets),
    VENUE_INFO(R.style.Theme_Squanchy_VenueInfo);

    private final int theme;

    BottomNavigationSection(@StyleRes int theme) {
        this.theme = theme;
    }

    @StyleRes
    public int theme() {
        return theme;
    }
}
