package net.squanchy.schedule.view;

import android.content.Context;
import android.util.AttributeSet;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.support.widget.CardLayout;

public abstract class EventItemView extends CardLayout {

    public EventItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void updateWith(Event event);
}
