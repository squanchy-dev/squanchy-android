package com.connfa.schedule;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.connfa.R;
import com.connfa.navigation.NavigationDrawerActivity;
import com.connfa.navigation.Navigator;
import com.connfa.schedule.navigation.ScheduleActivityNavigator;
import com.connfa.schedule.service.ScheduleActivityService;
import com.connfa.schedule.view.ScheduleView;
import com.connfa.schedule.view.ScheduleViewPagerAdapter;
import com.connfa.service.firebase.FirebaseConnfaRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ScheduleActivity extends NavigationDrawerActivity implements ScheduleViewPagerAdapter.OnEventClickedListener {

    private Navigator navigator;

    private ScheduleView scheduleView;
    private ScheduleActivityService service;
    private Disposable subscription;

    @Override
    protected void inflateActivityContent(ViewGroup parent) {
        LayoutInflater.from(this)
                .inflate(R.layout.activity_schedule, parent, true);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar(toolbar);

        scheduleView = (ScheduleView) findViewById(R.id.content_root);
        service = new ScheduleActivityService(FirebaseConnfaRepository.newInstance());

        navigator = new ScheduleActivityNavigator(this);
    }

    private void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.activity_schedule);
    }

    @Override
    protected void onStart() {
        super.onStart();

        subscription = service.schedule()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedule -> scheduleView.updateWith(schedule, this));
    }

    @Override
    protected void onStop() {
        super.onStop();

        subscription.dispose();
    }

    @Override
    protected Navigator navigate() {
        return navigator;
    }

    @Override
    public void onEventClicked() {
        navigate().toEventDetails();
    }
}
