package net.squanchy.eventdetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.squanchy.R;

public class EventDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_ID = "event_id";
    public static final String EXTRA_DAY = "day";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
