package net.squanchy.tweets.view;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.support.widget.CardLayout;
import net.squanchy.tweets.domain.view.Tweet;
import net.squanchy.tweets.util.TweetFormatter;
import net.squanchy.tweets.util.TwitterFooterFormatter;

public class TweetItemView extends CardLayout {

    private TextView tweetText;
    private TweetFooterView tweetFooter;

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
        tweetFooter = (TweetFooterView) findViewById(R.id.tweet_footer);

        tweetText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void updateWith(Tweet tweet) {
        tweetText.setText(TweetFormatter.format(tweet));
        tweetFooter.updateWith(tweet.user().photoUrl(), TwitterFooterFormatter.recapFrom(tweet, getContext()));
    }
}
