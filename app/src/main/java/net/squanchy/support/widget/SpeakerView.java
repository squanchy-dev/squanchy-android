package net.squanchy.support.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.squanchy.R;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.imageloader.ImageLoaderInjector;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Optional;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;
import static net.squanchy.support.lang.Lists.map;

public abstract class SpeakerView extends LinearLayout {

    @Nullable
    private ImageLoader imageLoader;

    private ViewGroup speakerPhotoContainer;
    private TextView speakerNameView;

    private final LayoutInflater layoutInflater;

    public SpeakerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeakerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SpeakerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if (!isInEditMode()) {
            AppCompatActivity activity = unwrapToActivityContext(context);
            imageLoader = ImageLoaderInjector.obtain(activity).imageLoader();
        }
        super.setOrientation(VERTICAL);

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("SpeakerView doesn't support changing orientation");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        speakerPhotoContainer = findViewById(R.id.speaker_photos_container);
        speakerNameView = findViewById(R.id.speaker_name);
    }

    public void updateWith(List<Speaker> speakers, Optional<OnSpeakerClickListener> listener) {
        speakerNameView.setText(toCommaSeparatedNames(speakers));
        updateSpeakerPhotos(speakers, listener);
    }

    private String toCommaSeparatedNames(List<Speaker> speakers) {
        return TextUtils.join(", ", map(speakers, Speaker::getName));
    }

    private void updateSpeakerPhotos(List<Speaker> speakers, Optional<OnSpeakerClickListener> listener) {
        if (imageLoader == null) {
            throw new IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet");
        }

        List<ImageView> photoViews;
        if (speakerPhotoContainer.getChildCount() > 0) {
            photoViews = getAllImageViewsContainedIn(speakerPhotoContainer);
            speakerPhotoContainer.removeAllViews();
        } else {
            photoViews = Collections.emptyList();
        }

        for (Speaker speaker : speakers) {
            ImageView photoView = recycleOrInflatePhotoView(photoViews);
            speakerPhotoContainer.addView(photoView);
            setClickListenerOrNotClickable(photoView, listener, speaker);

            if (speaker.getPhotoUrl().isPresent()) {
                loadSpeakerPhoto(photoView, speaker.getPhotoUrl().get(), imageLoader);
            } else {
                photoView.setImageResource(R.drawable.ic_speaker_no_avatar);
            }
        }
    }

    private void setClickListenerOrNotClickable(ImageView photoView, Optional<OnSpeakerClickListener> listener, Speaker speaker) {
        if (listener.isPresent()) {
            photoView.setOnClickListener(v -> listener.get().onSpeakerClicked(speaker));
            photoView.setClickable(true);
        } else {
            photoView.setOnClickListener(null);
            photoView.setClickable(false);
        }
    }

    private ImageView recycleOrInflatePhotoView(List<ImageView> photoViews) {
        if (photoViews.isEmpty()) {
            return inflatePhotoView(speakerPhotoContainer);
        } else {
            return photoViews.remove(0);
        }
    }

    protected abstract ImageView inflatePhotoView(ViewGroup speakerPhotoContainer);

    private void loadSpeakerPhoto(ImageView photoView, String photoUrl, ImageLoader imageLoader) {
        photoView.setImageDrawable(null);
        imageLoader.load(photoUrl)
                .into(photoView);
    }

    private List<ImageView> getAllImageViewsContainedIn(ViewGroup container) {
        int childCount = container.getChildCount();
        List<ImageView> children = new ArrayList<>(childCount);
        for (int i = 0; i < childCount; i++) {
            View child = container.getChildAt(i);
            children.add((ImageView) child);
        }
        return children;
    }

    protected final LayoutInflater layoutInflater() {
        return layoutInflater;
    }

    public interface OnSpeakerClickListener {

        void onSpeakerClicked(Speaker speaker);
    }
}
