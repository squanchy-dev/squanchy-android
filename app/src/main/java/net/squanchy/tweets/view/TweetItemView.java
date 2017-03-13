package net.squanchy.tweets.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;

import net.squanchy.R;
import net.squanchy.support.lang.Optional;
import net.squanchy.support.widget.CardLayout;

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
    }

    public void updateWith(Tweet tweet) {
        tweetText.setText(tweet.text);
        tweetTimestamp.setText(getTimestampFrom(tweet)
                .or(getContext().getString(R.string.tweet_date_not_available)));
    }

    private Optional<String> getTimestampFrom(Tweet displayTweet) {
        final String formattedTimestamp;
        final String date = displayTweet.createdAt;

        if (date != null && TwitterDateUtils.isValidTimestamp(date)) {
            final Long createdAtTimestamp = TwitterDateUtils.apiTimeToLong(date);
            formattedTimestamp = TwitterDateUtils.getRelativeTimeString(getResources(),
                    System.currentTimeMillis(), createdAtTimestamp);
        } else {
            formattedTimestamp = null;
        }

        return Optional.fromNullable(formattedTimestamp);
    }
}
