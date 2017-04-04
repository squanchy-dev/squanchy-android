package net.squanchy.tweets;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.List;

import net.squanchy.R;
import net.squanchy.home.Loadable;
import net.squanchy.navigation.Navigator;
import net.squanchy.tweets.domain.view.TweetViewModel;
import net.squanchy.tweets.service.TwitterService;
import net.squanchy.tweets.view.TweetsAdapter;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;

public class TweetsPageView extends CoordinatorLayout implements Loadable {

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
        super(context, attrs, defStyleAttr);
        AppCompatActivity activity = unwrapToActivityContext(getContext());
        TwitterComponent component = TwitterInjector.obtain(activity);
        twitterService = component.service();
        navigator = component.navigator();
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
    public void startLoading() {
        Context context = getContext();
        query = context.getString(R.string.social_query);
        emptyView.setText(context.getString(R.string.no_tweets_for_query, query));
        refreshTimeline();
    }

    @Override
    public void stopLoading() {
        subscription.dispose();
    }

    private void refreshTimeline() {
        swipeLayout.setRefreshing(true);
        refreshingData = true;
        subscription = twitterService.refresh(query)
                .subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess(List<TweetViewModel> tweet) {
        tweetsAdapter.updateWith(tweet, navigator::toTweet);
        onRefreshCompleted();
    }

    private void onError(Throwable throwable) {
        Timber.e(throwable, "Error refreshing the Twitter timeline");
        onRefreshCompleted();
    }

    private void onRefreshCompleted() {
        refreshingData = false;
        swipeLayout.setRefreshing(false);

        if (tweetsAdapter.isEmpty()) {
            emptyView.setVisibility(VISIBLE);
            tweetsList.setVisibility(GONE);
        } else {
            emptyView.setVisibility(GONE);
            tweetsList.setVisibility(VISIBLE);
        }
    }
}
