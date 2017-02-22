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
                return inflater.inflate(R.layout.schedule_view, parent, false);
            case HomeViewPagerAdapter.FAVOURITES_POSITION:
            case HomeViewPagerAdapter.TWEETS_POSITION:
            case HomeViewPagerAdapter.VENUE_POSITION:
            default:
                throw new IllegalArgumentException("Unsupported tab type for position " + position);
        }
    }
}
