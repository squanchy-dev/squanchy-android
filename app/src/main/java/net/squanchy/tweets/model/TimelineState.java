package net.squanchy.tweets.model;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

public class TimelineState {

    private Long nextCursor;
    private Long previousCursor;

    public static TimelineState init(List<Tweet> tweets) {
        Long minPosition = tweets.isEmpty() ? null : tweets.get(tweets.size() - 1).getId();
        Long maxPosition = tweets.isEmpty() ? null : tweets.get(0).getId();
        return new TimelineState(minPosition, maxPosition);
    }

    public TimelineState(Long nextCursor, Long previousCursor) {
        this.nextCursor = nextCursor;
        this.previousCursor = previousCursor;
    }

    public void resetCursors() {
        nextCursor = null;
        previousCursor = null;
    }

    public Long positionForNext() {
        return nextCursor == null ? null : nextCursor;
    }

    public Long positionForPrevious() {
        return previousCursor == null ? null : previousCursor;
    }

    public void setNextCursor(Long timelineCursor) {
        nextCursor = timelineCursor;
        setCursorsIfNull(timelineCursor);
    }

    public void previous(List<Tweet> tweets) {
        Long minPosition = tweets.isEmpty() ? null : tweets.get(tweets.size() - 1).getId();
        setPreviousCursor(minPosition);
    }

    private void setPreviousCursor(Long timelineCursor) {
        previousCursor = timelineCursor;
        setCursorsIfNull(timelineCursor);
    }

    private void setCursorsIfNull(Long timelineCursor) {
        if (nextCursor == null) {
            nextCursor = timelineCursor;
        }
        if (previousCursor == null) {
            previousCursor = timelineCursor;
        }
    }
}

    