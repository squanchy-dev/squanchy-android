package net.squanchy.tweets.view;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;

import net.squanchy.R;
import net.squanchy.support.widget.CardLayout;
import net.squanchy.tweets.util.TweetFormatter;
import net.squanchy.tweets.util.TwitterDateFormatter;

public class TweetItemView extends CardLayout {

    private TextView tweetText;
    private TextView tweetTimestamp;

    public TweetItemView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.cardViewDefaultStyle);
    }

    public TweetItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tweetText = (TextView) findViewById(R.id.tweet_text);
        tweetTimestamp = (TextView) findViewById(R.id.tweet_timestamp);

        tweetText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void updateWith(Tweet tweet) {
        tweetText.setText(TweetFormatter.format(tweet));
        tweetTimestamp.setText(TwitterDateFormatter.getTimestampFrom(tweet, getContext()));
    }
}
