package net.squanchy.tweets.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.internal.TimelineDelegate;

import net.squanchy.R;
import net.squanchy.tweets.TweetsPageView;

public class TweetsAdapter extends RecyclerView.Adapter<TweetViewHolder> {

    private final Context context;
    private final TimelineDelegate<Tweet> delegate;

    public TweetsAdapter(Timeline<Tweet> timeline, Context context) {
        this.context = context;
        this.delegate = new TimelineDelegate<>(timeline);
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        holder.updateWith(delegate.getItem(position));
    }

    @Override
    public int getItemCount() {
        return delegate.getCount();
    }

    public boolean isEmpty() {
        return delegate.getCount() == 0;
    }

    public void refresh(TweetsPageView.TimelineLoadingCallback timelineLoadingCallback) {
        delegate.refresh(timelineLoadingCallback);
    }
}
