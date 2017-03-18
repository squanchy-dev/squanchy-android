package net.squanchy.eventdetails.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
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
    private View descriptionHeader;
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
        descriptionHeader = findViewById(R.id.description_header);
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
        if (TextUtils.isGraphic(description)) {
            descriptionHeader.setVisibility(VISIBLE);
            descriptionTextView.setText(parseHtml(description));
        } else {
            descriptionHeader.setVisibility(GONE);
            descriptionTextView.setVisibility(GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressWarnings("deprecation")        // The older fromHtml() is only called pre-24
    private Spanned parseHtml(String description) {
        // TODO handle this properly
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(description);
        }
    }
}
