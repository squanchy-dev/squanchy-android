package net.squanchy.tweets.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.imageloader.ImageLoaderInjector;
import net.squanchy.support.ContextUnwrapper;
import net.squanchy.support.widget.CardLayout;
import net.squanchy.tweets.domain.view.TweetViewModel;
import net.squanchy.tweets.util.TwitterFooterFormatter;

public class TweetItemView extends CardLayout {

    @Nullable
    private ImageLoader imageLoader;

    private TextView tweetTextView;
    private TweetFooterView tweetFooterView;
    private ImageView tweetPhotoView;

    public TweetItemView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.cardViewDefaultStyle);
    }

    public TweetItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode()) {
            Activity activity = ContextUnwrapper.unwrapToActivityContext(context);
            imageLoader = ImageLoaderInjector.obtain(activity)
                    .imageLoader();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tweetTextView = (TextView) findViewById(R.id.tweet_text);
        tweetFooterView = (TweetFooterView) findViewById(R.id.tweet_footer);
        tweetPhotoView = (ImageView) findViewById(R.id.tweet_photo);

        tweetTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void updateWith(TweetViewModel tweet) {
        tweetTextView.setText(tweet.spannedText());
        tweetFooterView.updateWith(tweet.user().photoUrl(), TwitterFooterFormatter.recapFrom(tweet, getContext()));

        if (imageLoader == null) {
            throw new IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet");
        }

        tweetPhotoView.setImageDrawable(null);
        if (tweet.photoUrls().isEmpty()) {
            tweetPhotoView.setVisibility(GONE);
        } else {
            tweetPhotoView.setVisibility(VISIBLE);
            imageLoader.load(tweet.photoUrls().get(0)).into(tweetPhotoView);         // TODO make photoUrls be Optional<String> photoUrlx
        }
    }
}
