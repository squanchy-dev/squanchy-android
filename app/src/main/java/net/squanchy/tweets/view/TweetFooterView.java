package net.squanchy.tweets.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.imageloader.ImageLoaderInjector;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;

public class TweetFooterView extends LinearLayout {

    private ImageView photoView;
    private TextView recapView;

    @Nullable
    private ImageLoader imageLoader;

    public TweetFooterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TweetFooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TweetFooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if (!isInEditMode()) {
            Activity activity = unwrapToActivityContext(context);
            imageLoader = ImageLoaderInjector.obtain(activity).imageLoader();
        }

        super.setOrientation(HORIZONTAL);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("Changing orientation is not supported for TweetFooterView");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        photoView = (ImageView) findViewById(R.id.author_photo);
        recapView = (TextView) findViewById(R.id.recap_footer);
    }

    public void updateWith(String url, String formattedRecap) {
        recapView.setText(formattedRecap);
        if (imageLoader == null) {
            throw new IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet");
        }

        imageLoader.load(url).into(photoView);
    }
}
