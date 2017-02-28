package net.squanchy.navigation.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.squanchy.R;

class BottomTabsPagesFactory {

    static View inflate(Context context, ViewGroup parent, @Tab int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (position) {
            case HomeViewPagerAdapter.SCHEDULE_POSITION:
                return inflater.inflate(R.layout.view_page_schedule, parent, false);
            case HomeViewPagerAdapter.FAVORITES_POSITION:
                return inflater.inflate(R.layout.view_page_favorites, parent, false);
            case HomeViewPagerAdapter.TWEETS_POSITION:
                return inflater.inflate(R.layout.view_page_tweets, parent, false);
            case HomeViewPagerAdapter.VENUE_POSITION:
                return inflater.inflate(R.layout.view_page_venue, parent, false);
            default:
                throw new IllegalArgumentException("Unsupported tab type for position " + position);
        }
    }
}
