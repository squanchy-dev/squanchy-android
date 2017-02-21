package net.squanchy.schedule.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.viewpageradapter.ViewPagerAdapter;

import java.util.Collections;
import java.util.List;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.SchedulePage;

import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

public class ScheduleViewPagerAdapter extends ViewPagerAdapter<SchedulePageView> {

    private final Context context;
    private final Typeface tabTypeface;

    private List<SchedulePage> pages = Collections.emptyList();

    @Nullable
    private OnEventClickedListener listener;

    ScheduleViewPagerAdapter(Context context, Typeface tabTypeface) {
        this.context = context;
        this.tabTypeface = tabTypeface;
    }

    void updateWith(List<SchedulePage> pages, OnEventClickedListener listener) {
        this.pages = pages;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    protected SchedulePageView createView(ViewGroup container, int position) {
        return (SchedulePageView) LayoutInflater.from(context)
                .inflate(R.layout.page_schedule, container, false);
    }

    @Override
    protected void bindView(SchedulePageView view, int position) {
        List<Event> events = pages.get(position).events();
        view.updateWith(events, listener);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CalligraphyUtils.applyTypefaceSpan(pages.get(position).title(), tabTypeface);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public interface OnEventClickedListener {

        void onEventClicked(Event event);
    }
}
