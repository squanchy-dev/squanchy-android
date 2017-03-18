package net.squanchy.support.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.squanchy.R;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.imageloader.ImageLoaderInjector;
import net.squanchy.speaker.domain.view.Speaker;

import static net.squanchy.support.lang.Lists.map;

public abstract class SpeakerView extends LinearLayout {

    @Nullable
    private ImageLoader imageLoader;

    private ViewGroup speakerPhotoContainer;
    private TextView speakerNameView;

    protected final LayoutInflater layoutInflater;

    public SpeakerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeakerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SpeakerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if (!isInEditMode()) {
            imageLoader = ImageLoaderInjector.obtain(context).imageLoader();
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

        speakerPhotoContainer = (ViewGroup) findViewById(R.id.speaker_photos_container);
        speakerNameView = (TextView) findViewById(R.id.speaker_name);
    }

    public void updateWith(List<Speaker> speakers, OnSpeakerClickListener listener) {
        speakerNameView.setText(toCommaSeparatedNames(speakers));
        updateSpeakerPhotos(speakers, listener);
    }

    private String toCommaSeparatedNames(List<Speaker> speakers) {
        return TextUtils.join(", ", map(speakers, Speaker::name));
    }

    private void updateSpeakerPhotos(List<Speaker> speakers, OnSpeakerClickListener listener) {
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
            if (speaker.photoUrl().isPresent()) {
                photoView.setOnClickListener(v -> listener.onSpeakerClicked(speaker));
                loadSpeakerPhoto(photoView, speaker.photoUrl().get(), imageLoader);
            }
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
        if (isFirebaseStorageUrl(photoUrl)) {
            StorageReference photoReference = FirebaseStorage.getInstance().getReference(photoUrl);
            imageLoader.load(photoReference).into(photoView);
        } else {
            imageLoader.load(photoUrl).into(photoView);
        }
    }

    private boolean isFirebaseStorageUrl(String url) {
        return url.startsWith("gs://");            // TODO move elsewhere
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

    public interface OnSpeakerClickListener {

        void onSpeakerClicked(Speaker speaker);
    }
}
