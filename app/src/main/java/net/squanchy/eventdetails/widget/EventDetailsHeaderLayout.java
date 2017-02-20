package net.squanchy.eventdetails.widget;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;

import net.squanchy.schedule.domain.view.Event;

public class EventDetailsHeaderLayout extends CollapsingToolbarLayout {

    public EventDetailsHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventDetailsHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void updateWith(Event event) {
        setTitle(event.title());
    }
}
