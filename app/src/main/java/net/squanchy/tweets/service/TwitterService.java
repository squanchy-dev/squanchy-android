package net.squanchy.tweets.service;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.tweets.view.TimelineStateHolder;

import io.reactivex.Observable;

public class TwitterService {

    private final TwitterRepo repo;
    private final TimelineStateHolder timelineStateHolder;
    private List<Tweet> itemList;

    public TwitterService(TwitterRepo repo) {
        this.repo = repo;
        this.timelineStateHolder = new TimelineStateHolder();

        itemList = new ArrayList<>();
    }

    public Observable<Search> refresh() {
        timelineStateHolder.resetCursors();
        return load();
    }

    public Observable<Search> previous() {
        return loadPrevious(timelineStateHolder.positionForPrevious());
    }

    public int size() {
        return itemList.size();
    }

    public Tweet itemAt(int position) {
        return itemList.get(position);
    }

    private Observable<Search> load() {
        return repo.refresh();
    }

    private Observable<Search> loadPrevious(Long maxPosition) {
        return repo.previous(maxPosition);
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
