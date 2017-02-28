package net.squanchy.tweets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import net.squanchy.R;

import timber.log.Timber;

public class TweetsPageView extends LinearLayout {

    private ListView tweetsList;
    private TweetTimelineListAdapter tweetsAdapter;
    private SwipeRefreshLayout swipeLayout;
    private boolean refreshingData;

    public TweetsPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TweetsPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TweetsPageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        super.setOrientation(VERTICAL);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("TweetsPageView doesn't support changing orientation");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tweetsList = (ListView) findViewById(R.id.list);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_container);

        swipeLayout.setOnRefreshListener(() -> {
            if (refreshingData) {
                Timber.i("Timeline refresh already underway, ignoring new refresh request");
                return;
            }
            refreshTimeline();
        });
    }

//    @Override                        TODO move to page selected in HomeActivity
//    protected void onStart() {
//        super.onStart();
//        analytics.trackEvent("Social Feed screen", getString(R.string.action_open));
//    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Context context = getContext();
        SearchTimeline timeline = new SearchTimeline.Builder()
                .query(context.getString(R.string.social_query))
                .build();

        tweetsAdapter = new TweetTimelineListAdapter.Builder(context)
                .setTimeline(timeline)
                .build();

        tweetsList.setAdapter(tweetsAdapter);
    }

    private void refreshTimeline() {
        swipeLayout.setRefreshing(true);
        refreshingData = true;
        tweetsAdapter.refresh(new TimelineLoadingCallback());
    }

    private void onRefreshFinished() {
        refreshingData = false;
        swipeLayout.setRefreshing(false);
    }

    private class TimelineLoadingCallback extends Callback<TimelineResult<Tweet>> {

        @Override
        public void success(Result<TimelineResult<Tweet>> result) {
            onRefreshFinished();
        }

        @Override
        public void failure(TwitterException exception) {
            onRefreshFinished();
            Timber.e(exception, "Error while refreshing the timeline.");
            // TODO show empty state
        }
    }
}
