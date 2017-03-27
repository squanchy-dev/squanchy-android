package net.squanchy.schedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.eventdetails.widget.ExperienceLevelIconView;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.support.widget.SpeakerView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TalkEventItemView extends EventItemView {

    private TextView titleView;
    private TextView timestampView;
    private ExperienceLevelIconView experienceLevelIconView;
    private SpeakerView speakerView;

    public TalkEventItemView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.cardViewDefaultStyle);
    }

    public TalkEventItemView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    @Override
    public void updateWith(Event event) {
        ensureSupportedType(event.type());

        timestampView.setText(startTimeAsFormattedString(event));
        titleView.setText(event.title());

        if (event.experienceLevel().isPresent()) {
            experienceLevelIconView.setExperienceLevel(event.experienceLevel().get());
            experienceLevelIconView.setVisibility(VISIBLE);
        } else {
            experienceLevelIconView.setVisibility(INVISIBLE);
        }

        speakerView.setVisibility(event.speakers().isEmpty() ? GONE : VISIBLE);
        speakerView.updateWith(event.speakers(), speaker -> { });
    }

    private String startTimeAsFormattedString(Event event) {
        DateTimeFormatter formatter = DateTimeFormat.shortTime()
                .withZone(event.timeZone());

        return formatter.print(event.startTime().toDateTime());
    }

    private void ensureSupportedType(Event.Type type) {
        if (type == Event.Type.TALK || type == Event.Type.KEYNOTE) {
            return;
        }
        throw new IllegalArgumentException("Event with type " + type.name() + " is not supported by this view");
    }
}
