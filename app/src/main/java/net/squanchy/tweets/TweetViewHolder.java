package net.squanchy.tweets;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;

import net.squanchy.R;
import net.squanchy.tweets.view.TweetItemView;

public class TweetViewHolder extends ViewHolder {

    public TweetViewHolder(View itemView) {
        super(itemView);
    }

    void updateWith(Tweet tweet){
        ((TweetItemView) itemView).updateWith(tweet);
    }
}
