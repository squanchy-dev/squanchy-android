package com.connfa.schedule.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.connfa.R;
import com.connfa.schedule.domain.view.Event;
import com.connfa.schedule.domain.view.SchedulePage;
import com.novoda.viewpageradapter.ViewPagerAdapter;

import java.util.Collections;
import java.util.List;

public class ScheduleViewPagerAdapter extends ViewPagerAdapter<SchedulePageView> {

    private final Context context;

    private List<SchedulePage> pages = Collections.emptyList();

    public ScheduleViewPagerAdapter(Context context) {
        this.context = context;
    }

    public void updateWith(List<SchedulePage> pages) {
        this.pages = pages;
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
        view.updateWith(events);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position).title();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
