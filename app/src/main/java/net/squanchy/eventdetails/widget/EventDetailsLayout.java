package net.squanchy.eventdetails.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Event;

public class EventDetailsLayout extends LinearLayout {

    private static final String WHEN_DATE_TIME_FORMAT = "EEEE, d MMMM 'at' HH:mm";

    private TextView whenTextView;
    private View whereContainer;
    private TextView whereTextView;
    private TextView descriptionTextView;

    public EventDetailsLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventDetailsLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setOrientation(VERTICAL);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("Changing orientation is not supported for EventDetailsLayout");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View.inflate(getContext(), R.layout.merge_event_details_layout, this);

        whenTextView = (TextView) findViewById(R.id.when_text);
        whereTextView = (TextView) findViewById(R.id.where_text);
        whereContainer = findViewById(R.id.where_container);
        descriptionTextView = (TextView) findViewById(R.id.description_text);
    }

    public void updateWith(Event event) {
        whenTextView.setText(event.startTime().toString(WHEN_DATE_TIME_FORMAT));
        updateWhere(event);
        updateDescription(event.description());
    }

    private void updateWhere(Event event) {
        if (event.place().isPresent()) {
            whereContainer.setVisibility(VISIBLE);
            whereTextView.setText(event.place().get().name());
        } else {
            whereContainer.setVisibility(GONE);
        }
    }

    private void updateDescription(String description) {
        // TODO use Dante for this
        descriptionTextView.setText(Html.fromHtml(description));
    }
}
