package net.squanchy.speaker.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.speaker.domain.view.Speaker;

public class SpeakerDetailsLayout extends AppBarLayout {

    private SpeakerHeaderView headerView;
    private TextView bioView;

    public SpeakerDetailsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        headerView = (SpeakerHeaderView) findViewById(R.id.speaker_details_header);
        bioView = (TextView) findViewById(R.id.speaker_bio);
    }

    public void updateWith(Speaker speaker) {
        headerView.updateWith(speaker);
        bioView.setText(parseHtml(speaker.bio()));
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
