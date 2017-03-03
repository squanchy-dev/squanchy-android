package net.squanchy.search;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import net.squanchy.R;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.search.view.SpeakersView.OnSpeakerClickedListener;
import net.squanchy.speaker.domain.view.Speaker;

public class SearchItemView extends LinearLayout {

    private ImageView image;
    private TextView name;

    public SearchItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SearchItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        super.setOrientation(VERTICAL);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("SearchItem doesn't support changing orientation");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        image = (ImageView) findViewById(R.id.speaker_image);
        name = (TextView) findViewById(R.id.speaker_names);
    }

    public void updateWith(Speaker speaker, ImageLoader imageLoader, OnSpeakerClickedListener listener) {
        name.setText(speaker.fullName());
        updateSpeakerPhotos(speaker, imageLoader);
        setOnClickListener(v -> listener.onSpeakerClicked(speaker.id()));
    }

    private void updateSpeakerPhotos(Speaker speaker, ImageLoader imageLoader) {
        if (imageLoader == null) {
            throw new IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet");
        }

        loadPhoto(image, speaker.avatarImageURL(), imageLoader);
    }

    private void loadPhoto(ImageView photoView, String photoUrl, ImageLoader imageLoader) {
        //TODO load photoUrl here instead of the hardcoded resource
        StorageReference photoReference = FirebaseStorage.getInstance().getReference("speakers/squanchy.webp");
        imageLoader.load(photoReference).into(photoView);
    }
}
