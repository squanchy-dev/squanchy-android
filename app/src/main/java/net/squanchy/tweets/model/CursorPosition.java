package net.squanchy.tweets.model;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

public class CursorPosition {

    public final Long minPosition;
    public final Long maxPosition;

    public static CursorPosition from(List<Tweet> tweets) {
        Long minPosition = tweets.isEmpty() ? null : tweets.get(tweets.size() - 1).getId();
        Long maxPosition = tweets.isEmpty() ? null : tweets.get(0).getId();
        return new CursorPosition(minPosition, maxPosition);
    }

    private CursorPosition(long minPosition, long maxPosition) {
        this.minPosition = minPosition;
        this.maxPosition = maxPosition;
    }
}
