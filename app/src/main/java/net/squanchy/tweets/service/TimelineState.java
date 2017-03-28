package net.squanchy.tweets.service;

import android.support.annotation.Nullable;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

class TimelineState {

    @Nullable
    private Long previousCursor;

    static TimelineState create(List<Tweet> tweets) {
        Long minPosition = tweets.isEmpty() ? null : tweets.get(tweets.size() - 1).getId();
        return new TimelineState(minPosition);
    }

    private TimelineState(@Nullable Long previousCursor) {
        this.previousCursor = previousCursor;
    }

    @Nullable
    Long previous() {
        return previousCursor;
    }

    void setPrevious(List<Tweet> tweets) {
        previousCursor = lastTweetId(tweets);
    }

    @Nullable
    private Long lastTweetId(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
            return null;
        }
        return tweets.get(tweets.size() - 1).getId();
    }
}

    