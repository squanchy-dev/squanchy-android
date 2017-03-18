package net.squanchy.eventdetails.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Event;

import net.squanchy.schedule.domain.view.Event.Type;
import net.squanchy.support.widget.SpeakerView;

public class EventDetailsCoordinatorLayout extends CoordinatorLayout {

    private FloatingActionButton floatingActionButton;
    private EventDetailsHeaderLayout headerLayout;
    private EventDetailsLayout detailsLayout;

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
        detailsLayout = (EventDetailsLayout) findViewById(R.id.event_details);
    }

    public void updateWith(Event event, OnEventDetailsClickListener listener) {
        headerLayout.updateWith(event, listener);
        detailsLayout.updateWith(event);

        if (canBeFavorited(event)) {
            floatingActionButton.setImageResource(
                    event.favorited() ?
                            R.drawable.ic_favorite_filled :
                            R.drawable.ic_favorite_empty
            );
            floatingActionButton.setOnClickListener(v -> listener.onFavoriteClick());
            floatingActionButton.setVisibility(VISIBLE);
        } else {
            floatingActionButton.setVisibility(GONE);
        }
    }

    private boolean canBeFavorited(Event event) {
        Event.Type type = event.type();
        return type == Type.TALK || type == Type.KEYNOTE;
    }

    public interface OnEventDetailsClickListener extends OnFavoriteClickListener, SpeakerView.OnSpeakerClickListener {

    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick();
    }
}
