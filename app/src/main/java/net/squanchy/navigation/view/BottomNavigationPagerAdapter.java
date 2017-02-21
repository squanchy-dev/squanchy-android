package net.squanchy.navigation.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.viewpageradapter.ViewPagerAdapter;

import net.squanchy.schedule.ScheduleView;

public class BottomNavigationPagerAdapter extends ViewPagerAdapter<View> {

    private static final int SCHEDULE_POSITION = 0;
    private static final int FAVOURITES_POSITION = 1;
    private static final int TWEETS_POSITION = 2;
    private static final int VENUE_POSITION = 3;
    private final Context context;

    public BottomNavigationPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected View createView(ViewGroup container, int position) {
        if (position == SCHEDULE_POSITION) {
            return ScheduleView.inflate(context, container);
        }
        return null;
    }

    @Override
    protected void bindView(View view, int position) {
        // We don't need this, views automatically fetch data in their onAttachToWindow() method
    }

    @Override
    public int getCount() {
        return 1;
    }
}
