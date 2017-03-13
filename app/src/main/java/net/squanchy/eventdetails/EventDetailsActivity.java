package net.squanchy.eventdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import net.squanchy.R;
import net.squanchy.eventdetails.widget.EventDetailsCoordinatorLayout;
import net.squanchy.fonts.TypefaceStyleableActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class EventDetailsActivity extends TypefaceStyleableActivity {

    private static final String EXTRA_EVENT_ID = "event_id";

    private EventDetailsService service;
    private Disposable subscription;
    private EventDetailsCoordinatorLayout coordinatorLayout;

    public static Intent createIntent(Context context, String eventId) {
        Intent intent = new Intent(context, EventDetailsActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_details);

        setupToolbar();

        EventDetailsComponent component = EventDetailsInjector.obtain(this);
        service = component.service();

        coordinatorLayout = (EventDetailsCoordinatorLayout) findViewById(R.id.event_details_root);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String eventId = intent.getStringExtra(EXTRA_EVENT_ID);

        subscription = service.event(eventId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> coordinatorLayout.updateWith(event));
    }

    @Override
    protected void onStop() {
        super.onStop();

        subscription.dispose();
    }
}
