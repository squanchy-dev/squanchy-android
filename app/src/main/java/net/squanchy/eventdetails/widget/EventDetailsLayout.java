package net.squanchy.eventdetails.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.schedule.domain.view.Event;

public class EventDetailsLayout extends LinearLayout {

    private TextView placeView;
    private TextView speakersView;
    private TextView trackView;
    private ExperienceLevelIconView experienceLevelIconView;
    private TextView experienceLevelLabelView;

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

        placeView = (TextView) findViewById(R.id.place_view);
        speakersView = (TextView) findViewById(R.id.speakers_view);
        trackView = (TextView) findViewById(R.id.track_view);
        experienceLevelIconView = (ExperienceLevelIconView) findViewById(R.id.experience_level_icon);
        experienceLevelLabelView = (TextView) findViewById(R.id.experience_level_label);
    }

    public void updateWith(Event event) {
        // TODO create proper EventDetails model that we can use here
        placeView.setVisibility(event.placeVisibility());
        placeView.setText(event.place());
        speakersView.setVisibility(event.speakersVisibility());
        speakersView.setText(event.speakersNames());
        trackView.setVisibility(event.trackVisibility());
        experienceLevelIconView.setExperienceLevel(getEventBeginnerLevel());
        experienceLevelLabelView.setText(ExperienceLevel.BEGINNER.labelStringResId());
    }

    private String getEventBeginnerLevel() {
        return getContext().getString(ExperienceLevel.BEGINNER.labelStringResId());
    }
}
