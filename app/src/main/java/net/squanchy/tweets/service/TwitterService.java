package net.squanchy.tweets.service;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.tweets.model.SearchTimeline;
import net.squanchy.tweets.model.TimelineStateHolder;
import net.squanchy.tweets.model.Tweet;

public class TwitterService {

    private final SearchTimeline timeline;
    private final TimelineStateHolder timelineStateHolder;
    private List<Tweet> itemList = new ArrayList<>();

    public TwitterService(SearchTimeline timeline) {
        this.timeline = timeline;
        this.timelineStateHolder = new TimelineStateHolder();
    }

    public void refresh() {
        timelineStateHolder.resetCursors();
        load(timelineStateHolder.positionForNext());
    }

    public void previous() {
        loadPrevious(timelineStateHolder.positionForPrevious());
    }

    public int size() {
        return itemList.size();
    }

    public Tweet itemAt(int position) {
        return itemList.get(position);
    }

    private void load(Long minPosition) {
        if (timeline.isNotLoading()) {
            timeline.next(minPosition);
        }
    }

    private void loadPrevious(Long maxPosition) {
        if (timeline.isNotLoading()) {
            timeline.previous(maxPosition);
        }
    }
}
