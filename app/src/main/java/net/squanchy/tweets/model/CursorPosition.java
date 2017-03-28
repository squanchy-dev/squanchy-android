package net.squanchy.tweets.model;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

public class CursorPosition {

    private final Long minPosition;
    private final Long maxPosition;

    public static CursorPosition from(List<Tweet> tweets) {
        Long minPosition = tweets.isEmpty() ? null : tweets.get(tweets.size() - 1).getId();
        Long maxPosition = tweets.isEmpty() ? null : tweets.get(0).getId();
        return new CursorPosition(minPosition, maxPosition);
    }

    public static Long previousCursorFrom(List<Tweet> tweets) {
        return tweets.isEmpty() ? null : tweets.get(tweets.size() - 1).getId();
    }

    private CursorPosition(long minPosition, long maxPosition) {
        this.minPosition = minPosition;
        this.maxPosition = maxPosition;
    }
}
