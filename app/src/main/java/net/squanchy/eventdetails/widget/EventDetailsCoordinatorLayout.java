package net.squanchy.eventdetails.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Event;

public class EventDetailsCoordinatorLayout extends CoordinatorLayout {

    private FloatingActionButton floatingActionButton;
    private EventDetailsHeaderLayout headerLayout;

    public EventDetailsCoordinatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventDetailsCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        floatingActionButton = (FloatingActionButton) findViewById(R.id.favorite_fab);
        headerLayout = (EventDetailsHeaderLayout) findViewById(R.id.event_details_header);
    }

    public void updateWith(Event event) {
        headerLayout.updateWith(event);
    }
}
