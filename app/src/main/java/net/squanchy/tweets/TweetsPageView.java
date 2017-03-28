package net.squanchy.tweets;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import net.squanchy.R;
import net.squanchy.home.LifecycleView;
import net.squanchy.navigation.Navigator;
import net.squanchy.tweets.domain.view.Tweet;
import net.squanchy.tweets.service.TwitterService;
import net.squanchy.tweets.view.TweetsAdapter;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;

public class TweetsPageView extends LinearLayout implements LifecycleView {

    private final TwitterService twitterService;
    private final Navigator navigator;

    private TextView emptyView;
    private RecyclerView tweetsList;
    private TweetsAdapter tweetsAdapter;
    private SwipeRefreshLayout swipeLayout;
    private Disposable subscription;
    private String query;
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

        Activity activity = unwrapToActivityContext(context);
        TwitterComponent component = TwitterInjector.obtain(activity);
        twitterService = component.service();
        navigator = component.navigator();
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException(TweetsPageView.class.getSimpleName() + " doesn't support changing orientation");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setupToolbar();

        emptyView = (TextView) findViewById(R.id.empty_view);
        tweetsList = (RecyclerView) findViewById(R.id.tweet_feed);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_container);

        tweetsAdapter = new TweetsAdapter(getContext());
        tweetsList.setAdapter(tweetsAdapter);

        swipeLayout.setOnRefreshListener(() -> {
            if (refreshingData) {
                return;
            }
            refreshTimeline();
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_favorites);
        toolbar.inflateMenu(R.menu.homepage);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_search:
                    navigator.toSearch();
                    return true;
                case R.id.action_settings:
                    navigator.toSettings();
                    return true;
                default:
                    return false;
            }
        });
    }
    @Override
    public void onStart() {
        Context context = getContext();
        query = context.getString(R.string.social_query);
        emptyView.setText(context.getString(R.string.no_tweets_for_query, query));
        refreshTimeline();
    }

    @Override
    public void onStop() {
        subscription.dispose();
    }

    private void refreshTimeline() {
        swipeLayout.setRefreshing(true);
        refreshingData = true;
        subscription = twitterService.refresh(query)
                .subscribe(this::onRefreshFinished, this::onError);
    }

    private void onRefreshFinished(List<Tweet> tweets) {
        refreshingData = false;
        swipeLayout.setRefreshing(false);
        tweetsAdapter.updateWith(tweets);

        if (tweetsAdapter.isEmpty()) {
            emptyView.setVisibility(VISIBLE);
            tweetsList.setVisibility(GONE);
        } else {
            emptyView.setVisibility(GONE);
            tweetsList.setVisibility(VISIBLE);
        }
    }

    private void onError(Throwable throwable) {
        Timber.e(throwable);
        onRefreshFinished(Collections.emptyList());
    }
}
