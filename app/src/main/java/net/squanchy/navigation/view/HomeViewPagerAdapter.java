package net.squanchy.navigation.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.viewpageradapter.ViewPagerAdapter;

public class HomeViewPagerAdapter extends ViewPagerAdapter<View> {

    public static final int SCHEDULE_POSITION = 0;
    public static final int FAVORITES_POSITION = 1;
    public static final int TWEETS_POSITION = 2;
    public static final int VENUE_POSITION = 3;

    // TODO increment this value when a new view is added to the PagerAdapter
    private static final int NUMBER_OF_PAGES = 4;

    private final Context context;

    public HomeViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected View createView(ViewGroup container, @Tab int position) {
        return BottomTabsPagesFactory.inflate(context, container, position);
    }

    @Override
    protected void bindView(View view, int position) {
        // We don't need this, views automatically fetch data in their onAttachToWindow() method
    }

    @Override
    public int getCount() {
        return NUMBER_OF_PAGES;
    }
}
