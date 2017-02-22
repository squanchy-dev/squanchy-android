package net.squanchy.navigation.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.squanchy.R;

class BottomTabsPagesFactory {

    static View inflate(Context context, ViewGroup parent, @Tab int position){
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (position){
            case BottomNavigationPagerAdapter.SCHEDULE_POSITION:
                return inflater.inflate(R.layout.schedule_view, parent, false);
            case BottomNavigationPagerAdapter.FAVOURITES_POSITION:
            case BottomNavigationPagerAdapter.TWEETS_POSITION:
            case BottomNavigationPagerAdapter.VENUE_POSITION:
        }
        throw new IllegalArgumentException("Invalid position");
    }
}
