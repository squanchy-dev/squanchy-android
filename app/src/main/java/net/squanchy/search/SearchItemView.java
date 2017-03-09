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
import net.squanchy.search.view.SearchRecyclerView.OnSearchResultClickListener;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Optional;

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

        image = (ImageView) findViewById(R.id.speaker_photo);
        name = (TextView) findViewById(R.id.speaker_name);
    }

    public void updateWith(Speaker speaker, ImageLoader imageLoader, OnSearchResultClickListener listener) {
        name.setText(speaker.name());
        updateSpeakerPhotos(speaker, imageLoader);
        setOnClickListener(v -> listener.onSpeakerClicked(speaker));
    }

    private void updateSpeakerPhotos(Speaker speaker, ImageLoader imageLoader) {
        if (imageLoader == null) {
            throw new IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet");
        }

        Optional<String> avatarImageURL = speaker.avatarImageURL();
        if (avatarImageURL.isPresent()) {
            loadPhoto(image, avatarImageURL.get(), imageLoader);
        }
    }

    private void loadPhoto(ImageView photoView, String photoUrl, ImageLoader imageLoader) {
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
}
