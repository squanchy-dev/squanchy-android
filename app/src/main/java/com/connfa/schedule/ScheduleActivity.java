package com.connfa.schedule;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.connfa.R;
import com.connfa.navigation.NavigationDrawerActivity;
import com.connfa.navigation.Navigator;
import com.connfa.schedule.domain.view.SchedulePage;
import com.connfa.schedule.view.ScheduleViewPagerAdapter;

import java.util.Collections;

import org.joda.time.DateTime;

public class ScheduleActivity extends NavigationDrawerActivity {

    private ScheduleViewPagerAdapter viewPagerAdapter;

    @Override
    protected void inflateActivityContent(ViewGroup parent) {
        LayoutInflater.from(this)
                .inflate(R.layout.activity_schedule, parent, true);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabstrip);
        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter = new ScheduleViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.activity_schedule);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // TODO start fetching data
        viewPagerAdapter.updateWith(Collections.singletonList(SchedulePage.create(DateTime.now())));
    }

    @Override
    protected void onStop() {
        super.onStop();

        // TODO stop data flow
    }

    @Override
    protected Navigator navigate() {
        return new Navigator() {

            @Override
            public void up() {
                // TODO
            }
        };
    }

}
