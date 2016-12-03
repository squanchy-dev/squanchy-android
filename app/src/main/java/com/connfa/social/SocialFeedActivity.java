package com.connfa.social;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.connfa.R;
import com.connfa.analytics.Analytics;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import timber.log.Timber;

public class SocialFeedActivity extends AppCompatActivity {

    private Analytics analytics;

    private ListView tweetsList;
    private TweetTimelineListAdapter tweetsAdapter;
    private SwipeRefreshLayout swipeLayout;
    private boolean refreshingData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        analytics = Analytics.from(this);

        setContentView(R.layout.activity_social_feed);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        SearchTimeline timeline = new SearchTimeline.Builder()
                .query(getString(R.string.social_query))
                .build();

        tweetsAdapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(timeline)
                .build();

        tweetsList = (ListView) findViewById(R.id.list);
        tweetsList.setAdapter(tweetsAdapter);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_container);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (refreshingData) {
                    Timber.i("Timeline refresh already underway, ignoring new refresh request");
                    return;
                }
                refreshTimeline();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        analytics.trackEvent("Social Feed screen", getString(R.string.action_open));
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
