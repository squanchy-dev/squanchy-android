package net.squanchy.schedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;

import net.squanchy.R;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.imageloader.ImageLoaderInjector;

public class SpeakerView extends LinearLayout {

    private static final String SPEAKER_PHOTO_PATH_TEMPLATE = "speakers/%s";

    private final ImageLoader imageLoader;
    
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

        imageLoader = ImageLoaderInjector.obtain(context).imageLoader();
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
        String path = String.format(Locale.US, SPEAKER_PHOTO_PATH_TEMPLATE, "squanchy.webp");   // TODO use actual speaker ID
        StorageReference photoReference = FirebaseStorage.getInstance().getReference(path);
        imageLoader.load(photoReference).into(speakerPhotoView);
    }
}
