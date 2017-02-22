package net.squanchy.navigation.view;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef(value = {
        BottomNavigationPagerAdapter.SCHEDULE_POSITION,
        BottomNavigationPagerAdapter.FAVOURITES_POSITION,
        BottomNavigationPagerAdapter.TWEETS_POSITION,
        BottomNavigationPagerAdapter.VENUE_POSITION})
@Retention(RetentionPolicy.SOURCE)
@interface Tab {
    // Nothing to do, it's an IntDef annotation. Used by Lint only.
}
