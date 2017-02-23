package net.squanchy.navigation.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.squanchy.R;
import net.squanchy.schedule.ScheduleView;
import net.squanchy.search.OnSearchClickListener;

class BottomTabsPagesFactory {

    static View inflate(Context context, ViewGroup parent, @Tab int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (position) {
            case HomeViewPagerAdapter.SCHEDULE_POSITION:
                //TODO do this better
                ScheduleView view = (ScheduleView) inflater.inflate(R.layout.schedule_view, parent, false);
                view.setOnSearchClickListener((OnSearchClickListener) context);
                return view;
            case HomeViewPagerAdapter.FAVOURITES_POSITION:
            case HomeViewPagerAdapter.TWEETS_POSITION:
            case HomeViewPagerAdapter.VENUE_POSITION:
            default:
                throw new IllegalArgumentException("Unsupported tab type for position " + position);
        }
    }
}
