package net.squanchy.tweets.service;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Identifiable;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.internal.TimelineStateHolder;

import java.util.ArrayList;
import java.util.List;

public class TwitterService<T extends Identifiable> {

    private static final long CAPACITY = 200L;
    private final Timeline<T> timeline;
    private final TimelineStateHolder timelineStateHolder;
    private final List<T> itemList;

    public TwitterService(Timeline<T> timeline) {
        this.timeline = timeline;
        this.timelineStateHolder = new TimelineStateHolder();

        itemList = new ArrayList<>();
    }

    public void refresh(Callback<TimelineResult<T>> developerCb) {
        timelineStateHolder.resetCursors();
        load(timelineStateHolder.positionForNext(), new RefreshCallback(developerCb, timelineStateHolder));
    }

    public void previous(Callback<TimelineResult<T>> developerCb) {
        loadPrevious(timelineStateHolder.positionForPrevious(), new PreviousCallback(developerCb, timelineStateHolder));
    }

    public int size() {
        return itemList.size();
    }

    public T itemAt(int position) {
        return itemList.get(position);
    }

    private void load(Long minPosition, Callback<TimelineResult<T>> cb) {
        if (withinMaxCapacity()) {
            if (timelineStateHolder.startTimelineRequest()) {
                timeline.next(minPosition, cb);
            } else {
                cb.failure(new TwitterException("Request already in flight"));
            }
        } else {
            cb.failure(new TwitterException("Max capacity reached"));
        }
    }

    private boolean withinMaxCapacity() {
        return itemList.size() < CAPACITY;
    }

    private void loadPrevious(Long maxPosition, Callback<TimelineResult<T>> cb) {
        if (withinMaxCapacity()) {
            if (timelineStateHolder.startTimelineRequest()) {
                timeline.previous(maxPosition, cb);
            } else {
                cb.failure(new TwitterException("Request already in flight"));
            }
        } else {
            cb.failure(new TwitterException("Max capacity reached"));
        }
    }

    class DefaultCallback extends Callback<TimelineResult<T>> {

        final Callback<TimelineResult<T>> developerCallback;
        final TimelineStateHolder timelineStateHolder;

        DefaultCallback(Callback<TimelineResult<T>> developerCb, TimelineStateHolder timelineStateHolder) {
            this.developerCallback = developerCb;
            this.timelineStateHolder = timelineStateHolder;
        }

        @Override
        public void success(Result<TimelineResult<T>> result) {
            timelineStateHolder.finishTimelineRequest();
            developerCallback.success(result);
        }

        @Override
        public void failure(TwitterException exception) {
            timelineStateHolder.finishTimelineRequest();
            developerCallback.failure(exception);
        }
    }

    private class RefreshCallback extends DefaultCallback {

        RefreshCallback(Callback<TimelineResult<T>> developerCb, TimelineStateHolder timelineStateHolder) {
            super(developerCb, timelineStateHolder);
        }

        @Override
        public void success(Result<TimelineResult<T>> result) {
            if (!result.data.items.isEmpty()) {
                itemList.clear();
                itemList.addAll(result.data.items);
                timelineStateHolder.setNextCursor(result.data.timelineCursor);
            }
            super.success(result);
        }
    }

    private class PreviousCallback extends DefaultCallback {

        PreviousCallback(Callback<TimelineResult<T>> developerCb, TimelineStateHolder timelineStateHolder) {
            super(developerCb, timelineStateHolder);
        }

        @Override
        public void success(Result<TimelineResult<T>> result) {
            if (!result.data.items.isEmpty()) {
                itemList.addAll(result.data.items);
                timelineStateHolder.setPreviousCursor(result.data.timelineCursor);
            }
            super.success(result);
        }
    }
}
