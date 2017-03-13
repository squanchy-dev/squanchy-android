package net.squanchy.tweets;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;

import net.squanchy.R;

public class TweetViewHolder extends ViewHolder {

    private final TextView tweetView;

    public TweetViewHolder(View itemView) {
        super(itemView);
        tweetView = (TextView) itemView.findViewById(R.id.tweet);
    }

    void updateWith(Tweet tweet){
        tweetView.setText(tweet.text);
    }
}
