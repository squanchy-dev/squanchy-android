package net.squanchy.tweets.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Search;

import net.squanchy.R;
import net.squanchy.tweets.service.TwitterRepository;

import io.reactivex.Observable;

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final TwitterItemAdapter itemAdapter;

    public TweetsAdapter(TwitterRepository repo, Context context) {
        this.context = context;
        this.itemAdapter = new TwitterItemAdapter(repo);
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
        return itemAdapter.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = itemAdapter.getItemViewType(position);

        if (viewType == TweetViewTypeId.TWEET) {
            ((TweetViewHolder) holder).updateWith(itemAdapter.itemAt(position));
        } else if (viewType == TweetViewTypeId.LOADING) {
            /* do nothing */
        } else {
            throw new IllegalArgumentException("Item type " + viewType + " not supported");
        }
    }

    @Override
    public int getItemCount() {
        return itemAdapter.size();
    }

    public boolean isEmpty() {
        return itemAdapter.isEmpty();
    }

    public Observable<Search> refresh() {
        return itemAdapter.refresh();
    }

    public Observable<Search> previous() {
        return itemAdapter.previous();
    }
}
