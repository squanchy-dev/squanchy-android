package net.squanchy.speaker.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;

import net.squanchy.R;
import net.squanchy.speaker.domain.view.Speaker;

public class SpeakerDetailsLayout extends AppBarLayout {

    private SpeakerHeaderView speakerView;

    public SpeakerDetailsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        speakerView = (SpeakerHeaderView) findViewById(R.id.speaker_container);
    }

    public void updateWith(Speaker speaker) {
        speakerView.updateWith(speaker);
    }
}
