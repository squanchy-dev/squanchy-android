package net.squanchy.tweets.view;

import com.twitter.sdk.android.tweetui.TimelineCursor;

import java.util.concurrent.atomic.AtomicBoolean;

public class TimelineStateHolder {

    private TimelineCursor nextCursor;
    private TimelineCursor previousCursor;
    private final AtomicBoolean requestInFlight = new AtomicBoolean(false);

    public TimelineStateHolder() {
    }

    public void resetCursors() {
        nextCursor = null;
        previousCursor = null;
    }

    public Long positionForNext() {
        return nextCursor == null ? null : nextCursor.maxPosition;
    }

    public Long positionForPrevious() {
        return previousCursor == null ? null : previousCursor.minPosition;
    }

    public void setNextCursor(TimelineCursor timelineCursor) {
        nextCursor = timelineCursor;
        setCursorsIfNull(timelineCursor);
    }

    public void setPreviousCursor(TimelineCursor timelineCursor) {
        previousCursor = timelineCursor;
        setCursorsIfNull(timelineCursor);
    }

    private void setCursorsIfNull(TimelineCursor timelineCursor) {
        if (nextCursor == null) {
            nextCursor = timelineCursor;
        }
        if (previousCursor == null) {
            previousCursor = timelineCursor;
        }
    }

    public boolean startTimelineRequest() {
        return requestInFlight.compareAndSet(false, true);
    }

    public void finishTimelineRequest() {
        requestInFlight.set(false);
    }
}

