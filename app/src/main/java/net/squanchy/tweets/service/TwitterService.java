package net.squanchy.tweets.service;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TimelineResult;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.tweets.view.TimelineStateHolder;

public class TwitterService {

    private final Timeline<Tweet> timeline;
    private final TimelineStateHolder timelineStateHolder;
    private List<Tweet> itemList;

    public TwitterService(Timeline<Tweet> timeline) {
        this.timeline = timeline;
        this.timelineStateHolder = new TimelineStateHolder();

        itemList = new ArrayList<>();
    }

    public void refresh(Callback<TimelineResult<Tweet>> developerCb) {
        timelineStateHolder.resetCursors();
        load(timelineStateHolder.positionForNext(), new RefreshCallback(developerCb, timelineStateHolder));
    }

    public void previous(Callback<TimelineResult<Tweet>> developerCb) {
        loadPrevious(timelineStateHolder.positionForPrevious(), new PreviousCallback(developerCb, timelineStateHolder));
    }

    public int size() {
        return itemList.size();
    }

    public Tweet itemAt(int position) {
        return itemList.get(position);
    }

    private void load(Long minPosition, Callback<TimelineResult<Tweet>> cb) {
        if (timelineStateHolder.startTimelineRequest()) {
            timeline.next(minPosition, cb);
        } else {
            cb.failure(new TwitterException("Request already in flight"));
        }
    }

    private void loadPrevious(Long maxPosition, Callback<TimelineResult<Tweet>> cb) {
        if (timelineStateHolder.startTimelineRequest()) {
            timeline.previous(maxPosition, cb);
        } else {
            cb.failure(new TwitterException("Request already in flight"));
        }
    }

    class DefaultCallback extends Callback<TimelineResult<Tweet>> {

        final Callback<TimelineResult<Tweet>> developerCallback;
        final TimelineStateHolder timelineStateHolder;

        DefaultCallback(Callback<TimelineResult<Tweet>> developerCb, TimelineStateHolder timelineStateHolder) {
            this.developerCallback = developerCb;
            this.timelineStateHolder = timelineStateHolder;
        }

        @Override
        public void success(Result<TimelineResult<Tweet>> result) {
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

        RefreshCallback(Callback<TimelineResult<Tweet>> developerCb, TimelineStateHolder timelineStateHolder) {
            super(developerCb, timelineStateHolder);
        }

        @Override
        public void success(Result<TimelineResult<Tweet>> result) {
            if (!result.data.items.isEmpty()) {
                itemList.clear();
                final ArrayList<Tweet> receivedItems = new ArrayList<>(result.data.items);
                receivedItems.addAll(itemList);
                itemList = receivedItems;
                timelineStateHolder.setNextCursor(result.data.timelineCursor);
            }
            super.success(result);
        }
    }

    private class PreviousCallback extends DefaultCallback {

        PreviousCallback(Callback<TimelineResult<Tweet>> developerCb, TimelineStateHolder timelineStateHolder) {
            super(developerCb, timelineStateHolder);
        }

        @Override
        public void success(Result<TimelineResult<Tweet>> result) {
            if (!result.data.items.isEmpty()) {
                timelineStateHolder.setPreviousCursor(result.data.timelineCursor);
                itemList.addAll(result.data.items);
            }
            super.success(result);
        }
    }
}
