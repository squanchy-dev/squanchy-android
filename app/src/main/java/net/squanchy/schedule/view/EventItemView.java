package net.squanchy.schedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.eventdetails.widget.ExperienceLevelIconView;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.support.widget.CardLayout;

public class EventItemView extends CardLayout {

    private TextView titleView;
    private TextView timestampView;
    private ExperienceLevelIconView experienceLevelIconView;
    private ImageView speakerPhotoView;
    private TextView speakerNameView;

    public EventItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        timestampView = (TextView) findViewById(R.id.timestamp);
        titleView = (TextView) findViewById(R.id.title);
        experienceLevelIconView = (ExperienceLevelIconView) findViewById(R.id.experience_level_icon);
        speakerPhotoView = (ImageView) findViewById(R.id.speaker_photo);
        speakerNameView = (TextView) findViewById(R.id.speaker_name);
    }

    void updateWith(Event event) {
        timestampView.setText("12:00");         // TODO put start time 
        titleView.setText(event.title());
        experienceLevelIconView.setExperienceLevel(event.experienceLevel());

        speakerPhotoView.setVisibility(event.speakersVisibility());     // TODO bind photo
        speakerNameView.setVisibility(event.speakersVisibility());
        speakerNameView.setText(event.speakersNames());
    }
}
