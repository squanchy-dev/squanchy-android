package net.squanchy.tweets.domain;

import com.google.auto.value.AutoValue;
import com.twitter.sdk.android.core.models.Tweet;

@AutoValue
public abstract class TweetLinkInfo {

    public static TweetLinkInfo from(Tweet tweet) {
        return new AutoValue_TweetLinkInfo(tweet.idStr, tweet.user.screenName);
    }

    public abstract String statusId();

    public abstract String screenName();
}
