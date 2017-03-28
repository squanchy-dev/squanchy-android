package net.squanchy.tweets.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.R;
import net.squanchy.tweets.domain.view.Tweet;
import net.squanchy.tweets.service.TwitterRepository;
import net.squanchy.tweets.service.TwitterService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetViewHolder> {

    private final Context context;
    private final TwitterService twitterService;
    private List<Tweet> tweetList;

    public TweetsAdapter(TwitterRepository repo, Context context) {
        this.context = context;
        this.twitterService = new TwitterService(repo);
        this.tweetList = new ArrayList<>();
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        holder.updateWith(tweetList.get(position));
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public boolean isEmpty() {
        return tweetList.isEmpty();
    }

    public Observable<List<Tweet>> refresh() {
        return twitterService.refresh()
                .doOnNext(this::onRefreshSuccess);
    }

    private void onRefreshSuccess(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
            return;
        }

        tweetList.clear();
        final ArrayList<Tweet> receivedItems = new ArrayList<>(tweets);
        receivedItems.addAll(tweetList);
        tweetList = receivedItems;
    }
}
