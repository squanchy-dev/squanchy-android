package net.squanchy.schedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.squanchy.R;
import net.squanchy.support.widget.SpeakerView;

public class ScheduleSpeakerView extends SpeakerView {

    public ScheduleSpeakerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleSpeakerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ScheduleSpeakerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected ImageView inflatePhotoView(ViewGroup speakerPhotoContainer) {
        return (ImageView) layoutInflater().inflate(R.layout.view_speaker_photo_schedule, speakerPhotoContainer, false);
    }
}
