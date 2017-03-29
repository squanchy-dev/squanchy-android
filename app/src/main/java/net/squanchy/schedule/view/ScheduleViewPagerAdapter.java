package net.squanchy.schedule.view;

import android.content.Context;
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

import org.joda.time.LocalDateTime;

public class ScheduleViewPagerAdapter extends ViewPagerAdapter<ScheduleDayPageView> {

    private static final String TITLE_FORMAT_TEMPLATE = "EEE d";

    private final Context context;

    private List<SchedulePage> pages = Collections.emptyList();

    @Nullable
    private OnEventClickedListener listener;

    public ScheduleViewPagerAdapter(Context context) {
        this.context = context;
    }

    public void updateWith(List<SchedulePage> pages, OnEventClickedListener listener) {
        this.pages = pages;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    protected ScheduleDayPageView createView(ViewGroup container, int position) {
        return (ScheduleDayPageView) LayoutInflater.from(context)
                .inflate(R.layout.view_page_schedule_day, container, false);
    }

    @Override
    protected void bindView(ScheduleDayPageView view, int position) {
        List<Event> events = pages.get(position).events();
        view.updateWith(events, listener);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        LocalDateTime date = pages.get(position).date();
        return date.toString(TITLE_FORMAT_TEMPLATE).toUpperCase();
    }

    public String getPageDayId(int position) {
        return pages.get(position).dayId();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public interface OnEventClickedListener {

        void onEventClicked(Event event);
    }
}
