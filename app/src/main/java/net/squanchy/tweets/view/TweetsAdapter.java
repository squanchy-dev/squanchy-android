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
import net.squanchy.tweets.service.TwitterService;

public class TweetsAdapter extends RecyclerView.Adapter<TweetViewHolder> {

    private final Context context;
    private final TwitterService<Tweet> repository;

    public TweetsAdapter(Timeline<Tweet> timeline, Context context) {
        this.context = context;
        this.repository = new TwitterService<>(timeline);
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        holder.updateWith(repository.itemAt(position));
    }

    @Override
    public int getItemCount() {
        return repository.size();
    }

    public boolean isEmpty() {
        return repository.size() == 0;
    }

    public void refresh(TweetsPageView.TimelineLoadingCallback timelineLoadingCallback) {
        repository.refresh(timelineLoadingCallback);
    }

    public void previous(TweetsPageView.TimelineLoadingCallback timelineLoadingCallback) {
        repository.previous(timelineLoadingCallback);
    }
}
