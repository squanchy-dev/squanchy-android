package net.squanchy.tweets.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;

import net.squanchy.R;
import net.squanchy.support.widget.CardLayout;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TweetItemView extends CardLayout {

    private static final String TWITTER_DATE_PATTERN = "EEE MMM dd HH:mm:ss Z YYYY";

    private final DateTimeFormatter dateTimeFormatter;

    private TextView tweetText;
    private TextView tweetTimestamp;

    public TweetItemView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.cardViewDefaultStyle);
    }

    public TweetItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.dateTimeFormatter = DateTimeFormat.forPattern(TWITTER_DATE_PATTERN).withZoneUTC();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tweetText = (TextView) findViewById(R.id.tweet_text);
        tweetTimestamp = (TextView) findViewById(R.id.tweet_timestamp);
    }

    public void updateWith(Tweet tweet) {
        tweetText.setText(tweet.text);
        tweetTimestamp.setText(getTimestampFrom(tweet));
    }

    private String getTimestampFrom(Tweet displayTweet) {
        final String formattedTimestamp;
        if (displayTweet != null && displayTweet.createdAt != null &&
                TwitterDateUtils.isValidTimestamp(displayTweet.createdAt)) {
            final Long createdAtTimestamp = TwitterDateUtils.apiTimeToLong(displayTweet.createdAt);
            final String timestamp = TwitterDateUtils.getRelativeTimeString(getResources(),
                    System.currentTimeMillis(), createdAtTimestamp);
            formattedTimestamp = TwitterDateUtils.dotPrefix(timestamp);
        } else {
            formattedTimestamp = "";
        }

        return formattedTimestamp;
    }
}
