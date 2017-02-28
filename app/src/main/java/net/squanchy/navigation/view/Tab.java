package net.squanchy.navigation.view;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef(value = {
        HomeViewPagerAdapter.SCHEDULE_POSITION,
        HomeViewPagerAdapter.FAVORITES_POSITION,
        HomeViewPagerAdapter.TWEETS_POSITION,
        HomeViewPagerAdapter.VENUE_POSITION})
@Retention(RetentionPolicy.SOURCE)
@interface Tab {
    // Nothing to do, it's an IntDef annotation. Used by Lint only.
}
