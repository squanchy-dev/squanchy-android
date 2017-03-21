package net.squanchy.tweets.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;

import net.squanchy.R;
import net.squanchy.tweets.TweetsPageView;

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final TwitterRepository repository;

    public TweetsAdapter(Timeline<Tweet> timeline, Context context) {
        this.context = context;
        this.repository = new TwitterRepository(timeline);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @TweetViewTypeId int viewType) {
        if (viewType == TweetViewTypeId.TWEET) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
            return new TweetViewHolder(view);
        } else if (viewType == TweetViewTypeId.LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_tweet_loading, parent, false);
            return new LoadingViewHolder(view);
        } else {
            throw new IllegalArgumentException("Item type " + viewType + " not supported");
        }
    }

    @Override
    @TweetViewTypeId
    public int getItemViewType(int position) {
        return repository.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = repository.getItemViewType(position);

        if (viewType == TweetViewTypeId.TWEET) {
            ((TweetViewHolder) holder).updateWith(repository.itemAt(position));
        } else if (viewType == TweetViewTypeId.LOADING) {
            /* do nothing */
        } else {
            throw new IllegalArgumentException("Item type " + viewType + " not supported");
        }
    }

    @Override
    public int getItemCount() {
        return repository.size();
    }

    public boolean isEmpty() {
        return repository.isEmpty();
    }

    public void refresh(TweetsPageView.TimelineLoadingCallback timelineLoadingCallback) {
        repository.refresh(timelineLoadingCallback);
    }

    public void previous(TweetsPageView.TimelineLoadingCallback timelineLoadingCallback) {
        repository.previous(timelineLoadingCallback);
    }
}
