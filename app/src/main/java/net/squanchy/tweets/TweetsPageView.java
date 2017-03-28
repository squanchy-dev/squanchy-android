package net.squanchy.tweets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.tweets.service.TwitterRepository;
import net.squanchy.tweets.view.ScrollListener;
import net.squanchy.tweets.view.TweetsAdapter;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class TweetsPageView extends LinearLayout {

    private TextView emptyView;
    private RecyclerView tweetsList;
    private TweetsAdapter tweetsAdapter;
    private SwipeRefreshLayout swipeLayout;
    private TweetScrollListener scrollListener;
    private Disposable disposable;
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
        throw new UnsupportedOperationException(TweetsPageView.class.getSimpleName() + " doesn't support changing orientation");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        emptyView = (TextView) findViewById(R.id.empty_view);
        tweetsList = (RecyclerView) findViewById(R.id.tweet_feed);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_container);

        swipeLayout.setOnRefreshListener(() -> {
            if (refreshingData) {
                return;
            }
            refreshTimeline();
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initList();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        scrollListener.destroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void initList() {

        Context context = getContext();
        String query = context.getString(R.string.social_query);

        TwitterRepository repo = new TwitterRepository(query);
        tweetsAdapter = new TweetsAdapter(repo, context);
        tweetsList.setAdapter(tweetsAdapter);

        emptyView.setText(context.getString(R.string.no_tweets_for_query, query));

        LinearLayoutManager layoutManager = (LinearLayoutManager) tweetsList.getLayoutManager();
        scrollListener = new TweetScrollListener(layoutManager);
        tweetsList.addOnScrollListener(scrollListener);
        refreshTimeline();
    }

    private void refreshTimeline() {
        swipeLayout.setRefreshing(true);
        refreshingData = true;
        scrollListener.reset();
        disposable = tweetsAdapter.refresh()
                .subscribe(l -> onRefreshFinished(), this::onError);
    }

    private void onRefreshFinished() {
        refreshingData = false;

        if (tweetsAdapter.isEmpty()) {
            emptyView.setVisibility(VISIBLE);
            tweetsList.setVisibility(GONE);
        } else {
            emptyView.setVisibility(GONE);
            tweetsList.setVisibility(VISIBLE);
        }

        swipeLayout.setRefreshing(false);
        tweetsAdapter.notifyDataSetChanged();
    }

    private void onError(Throwable throwable) {
        Timber.e(throwable);
        onRefreshFinished();
    }

    private class TweetScrollListener extends ScrollListener {

        private Disposable disposable;

        TweetScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        protected void loadMore() {
            if (refreshingData) {
                return;
            }
            disposable = tweetsAdapter.previous()
                    .subscribe(search -> onRefreshFinished(), TweetsPageView.this::onError);
        }

        void destroy() {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }
}
