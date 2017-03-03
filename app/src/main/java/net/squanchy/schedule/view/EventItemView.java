package net.squanchy.schedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.squanchy.R;
import net.squanchy.eventdetails.widget.ExperienceLevelIconView;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.support.widget.CardLayout;

public class EventItemView extends CardLayout {

    private static final String SPEAKER_PHOTO_PATH_TEMPLATE = "speakers/%s";

    private TextView titleView;
    private TextView timestampView;
    private ExperienceLevelIconView experienceLevelIconView;
    private SpeakerView speakerView;

    public EventItemView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.cardViewDefaultStyle);
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
        speakerView = (SpeakerView) findViewById(R.id.speaker_container);
    }

    void updateWith(Event event) {
        timestampView.setText("12:00");         // TODO put start time 
        titleView.setText(event.title());
        experienceLevelIconView.setExperienceLevel(event.experienceLevel());

        speakerView.setVisibility(event.speakersVisibility());
        speakerView.updateWith(createDummySpeakersList());                      // TODO use real data
    }

    private List<Speaker> createDummySpeakersList() {
        FirebaseSpeaker firstSpeaker = new FirebaseSpeaker();
        firstSpeaker.avatarImageURL = String.format(Locale.US, SPEAKER_PHOTO_PATH_TEMPLATE, "squanchy.webp");
        firstSpeaker.speakerId = 0L;
        firstSpeaker.firstName = "Dave";
        firstSpeaker.lastName = "Clements";
        FirebaseSpeaker secondSpeaker = new FirebaseSpeaker();
        secondSpeaker.avatarImageURL = String.format(Locale.US, SPEAKER_PHOTO_PATH_TEMPLATE, "squanchy.webp");
        secondSpeaker.speakerId = 0L;
        secondSpeaker.firstName = "Qi";
        secondSpeaker.lastName = "Qu";
        return Arrays.asList(Speaker.create(firstSpeaker), Speaker.create(secondSpeaker));
    }
}
