package com.connfa.schedule.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.connfa.R;
import com.connfa.schedule.domain.view.SchedulePage;
import com.connfa.schedule.domain.view.Event;

import java.util.Collections;
import java.util.List;

public class ScheduleViewPagerAdapter extends PagerAdapter {

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
    public Object instantiateItem(ViewGroup container, int position) {
        SchedulePageView pageView = (SchedulePageView) LayoutInflater.from(context)
                .inflate(R.layout.page_schedule, container, false);

        List<Event> events = pages.get(position).events();
        pageView.updateWith(events);
        container.addView(pageView);

        return pageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position).formattedTitle(context);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
