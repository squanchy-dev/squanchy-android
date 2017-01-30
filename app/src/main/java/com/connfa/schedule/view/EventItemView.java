package com.connfa.schedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.connfa.R;
import com.connfa.schedule.domain.view.Event;

public class EventItemView extends FrameLayout {

    private TextView titleView;
    private TextView placeView;
    private View placeContainer;
    private View trackView;
    private TextView speakersView;
    private View speakersContainer;
    private ImageView experienceIconView;

    public EventItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        titleView = (TextView) findViewById(R.id.txtTitle);
        placeView = (TextView) findViewById(R.id.txtPlace);
        placeContainer = findViewById(R.id.layout_place);
        trackView = findViewById(R.id.txtTrack);
        speakersView = (TextView) findViewById(R.id.txtSpeakers);
        speakersContainer = findViewById(R.id.layout_speakers);
        experienceIconView = (ImageView) findViewById(R.id.imgExperience);
    }

    public void updateWith(Event event) {
        titleView.setText(event.title());

        placeView.setText(event.place());
        placeContainer.setVisibility(event.placeVisibility());

        trackView.setVisibility(event.trackVisibility());

        speakersView.setText(event.speakers());
        speakersContainer.setVisibility(event.speakersVisibility());

        experienceIconView.setImageResource(event.experienceLevelIcon());
    }
}
