package net.squanchy.schedule;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.squanchy.R;
import net.squanchy.navigation.NavigationDrawerActivity;
import net.squanchy.navigation.Navigator;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.view.ScheduleView;
import net.squanchy.schedule.view.ScheduleViewPagerAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ScheduleActivity extends NavigationDrawerActivity implements ScheduleViewPagerAdapter.OnEventClickedListener {

    private Navigator navigator;

    private ScheduleView scheduleView;
    private ScheduleService service;
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
        ScheduleComponent component = ScheduleInjector.obtain(this);

        service = component.service();
        navigator = component.navigator();
    }

    private void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_schedule);
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
    public void onEventClicked(Event event) {
        navigate().toEventDetails(event.day(), (int) event.id());
    }
}
