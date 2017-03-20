package net.squanchy.speaker.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.imageloader.ImageLoaderInjector;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Optional;

public class SpeakerHeaderView extends LinearLayout {

    @Nullable
    private ImageLoader imageLoader;

    private ImageView photoView;
    private TextView nameView;
    private TextView companyView;

    public SpeakerHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeakerHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SpeakerHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if (!isInEditMode()) {
            imageLoader = ImageLoaderInjector.obtain(context).imageLoader();
        }

        super.setOrientation(HORIZONTAL);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("Changing orientation is not supported for SpeakerHeaderView");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        photoView = (ImageView) findViewById(R.id.speaker_photo);
        nameView = (TextView) findViewById(R.id.speaker_name);
        companyView = (TextView) findViewById(R.id.speaker_company);
    }

    public void updateWith(Speaker speaker) {
        updatePhoto(speaker.photoUrl());

        nameView.setText(speaker.name());

        Optional<String> companyName = speaker.companyName();
        if (companyName.isPresent()) {
            // TODO support navigating to company website
            companyView.setText(companyName.get());
            companyView.setVisibility(VISIBLE);
        } else {
            companyView.setVisibility(GONE);
        }
    }

    private void updatePhoto(Optional<String> photoUrl) {
        if (imageLoader == null) {
            throw new IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet");
        }

        if (photoUrl.isPresent()) {
            photoView.setVisibility(VISIBLE);
            imageLoader.load(photoUrl.get()).into(photoView);
        } else {
            photoView.setVisibility(GONE);
        }
    }
}
