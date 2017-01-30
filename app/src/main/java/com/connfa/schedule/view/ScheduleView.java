package com.connfa.schedule.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.connfa.R;
import com.connfa.schedule.domain.view.Schedule;

public class ScheduleView extends CoordinatorLayout {

    private ScheduleViewPagerAdapter viewPagerAdapter;
    private View progressBar;

    public ScheduleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        progressBar = findViewById(R.id.progressbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabstrip);
        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter = new ScheduleViewPagerAdapter(getContext());
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void updateWith(Schedule schedule) {
        viewPagerAdapter.updateWith(schedule.pages());
        progressBar.setVisibility(GONE);
    }
}
