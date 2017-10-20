package net.squanchy.search;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        image = findViewById(R.id.speaker_photo);
        name = findViewById(R.id.speaker_name);
    }

    public void updateWith(Speaker speaker, ImageLoader imageLoader, OnSearchResultClickListener listener) {
        name.setText(speaker.getName());
        updateSpeakerPhotos(speaker, imageLoader);
        setOnClickListener(v -> listener.onSpeakerClicked(speaker));
    }

    private void updateSpeakerPhotos(Speaker speaker, ImageLoader imageLoader) {
        if (imageLoader == null) {
            throw new IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet");
        }

        Optional<String> avatarImageURL = speaker.getPhotoUrl();
        if (avatarImageURL.isPresent()) {
            imageLoader.load(avatarImageURL.get()).into(image);
        } else {
            image.setImageResource(R.drawable.ic_no_avatar);
        }
    }
}
