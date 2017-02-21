package net.squanchy.schedule.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.squanchy.R;

public class SpeakerView extends LinearLayout {

    private ImageView speakerPhotoView;
    private TextView speakerNameView;

    public SpeakerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeakerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SpeakerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        super.setOrientation(HORIZONTAL);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("SpeakerView doesn't support changing orientation");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        speakerPhotoView = (ImageView) findViewById(R.id.speaker_photo);
        speakerNameView = (TextView) findViewById(R.id.speaker_name);
    }

    public void updateWith(String speakersNames) {
        speakerNameView.setText(speakersNames);
        speakerPhotoView.setImageDrawable(new ColorDrawable(0xFF67B6E2)); // TODO bind photo too
    }
}
