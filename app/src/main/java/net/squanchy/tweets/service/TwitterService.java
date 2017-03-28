package net.squanchy.tweets.service;

import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.tweets.view.TimelineStateHolder;

import io.reactivex.Observable;

public class TwitterService {

    private final TwitterRepository repo;
    private final TimelineStateHolder timelineStateHolder;
    private List<Tweet> itemList;

    public TwitterService(TwitterRepository repo) {
        this.repo = repo;
        this.timelineStateHolder = new TimelineStateHolder();

        itemList = new ArrayList<>();
    }

    public Observable<Search> refresh() {
        timelineStateHolder.resetCursors();
        return repo.refresh()
                .doOnNext(this::onRefreshSuccess);
    }

    public Observable<Search> previous() {
        return repo.previous(timelineStateHolder.positionForPrevious())
                .doOnNext(this::onPreviousSuccess);
    }

    public int size() {
        return itemList.size();
    }

    public Tweet itemAt(int position) {
        return itemList.get(position);
    }

    private void onRefreshSuccess(Search search) {
        if (!search.tweets.isEmpty()) {
            itemList.clear();
            final ArrayList<Tweet> receivedItems = new ArrayList<>(search.tweets);
            receivedItems.addAll(itemList);
            itemList = receivedItems;
            //timelineStateHolder.setNextCursor(result.data.timelineCursor);
        }
    }

    private void onPreviousSuccess(Search search) {
        if (!search.tweets.isEmpty()) {
            //timelineStateHolder.setPreviousCursor(result.data.timelineCursor);
            itemList.addAll(search.tweets);
        }
    }
}
