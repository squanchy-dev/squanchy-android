package net.squanchy.tweets.view;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import net.squanchy.tweets.domain.view.TweetViewModel;

class TweetViewHolder extends ViewHolder {

    TweetViewHolder(View itemView) {
        super(itemView);
    }

    void updateWith(TweetViewModel tweet) {
        ((TweetItemView) itemView).updateWith(tweet);
    }
}
