package net.squanchy.tweets.view;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import com.twitter.sdk.android.core.models.Tweet;

class TweetViewHolder extends ViewHolder {

    TweetViewHolder(View itemView) {
        super(itemView);
    }

    void updateWith(Tweet tweet) {
        ((TweetItemView) itemView).updateWith(tweet);
    }
}
