package net.squanchy.schedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.eventdetails.widget.ExperienceLevelView;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.support.lang.Optional;
import net.squanchy.support.widget.SpeakerView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TalkEventItemView extends EventItemView {

    private TextView titleView;
    private TextView timestampView;
    private ExperienceLevelView experienceLevelView;
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
        experienceLevelView = (ExperienceLevelView) findViewById(R.id.experience_level);
        speakerView = (SpeakerView) findViewById(R.id.speaker_container);
    }

    @Override
    public void updateWith(Event event) {
        ensureSupportedType(event.getType());

        timestampView.setText(startTimeAsFormattedString(event));
        titleView.setText(event.getTitle());

        if (event.getExperienceLevel().isPresent()) {
            experienceLevelView.setExperienceLevel(event.getExperienceLevel().get());
            experienceLevelView.setVisibility(VISIBLE);
        } else {
            experienceLevelView.setVisibility(INVISIBLE);
        }

        speakerView.setVisibility(event.getSpeakers().isEmpty() ? GONE : VISIBLE);
        speakerView.updateWith(event.getSpeakers(), Optional.absent());
    }

    private String startTimeAsFormattedString(Event event) {
        DateTimeFormatter formatter = DateTimeFormat.shortTime()
                .withZone(event.getTimeZone());

        return formatter.print(event.getStartTime().toDateTime());
    }

    private void ensureSupportedType(Event.Type type) {
        if (type == Event.Type.TALK || type == Event.Type.KEYNOTE) {
            return;
        }
        throw new IllegalArgumentException("Event with type " + type.name() + " is not supported by this view");
    }
}
