package net.squanchy.tweets.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.squanchy.tweets.domain.TweetLinkInfo
import net.squanchy.tweets.domain.view.TweetViewModel

class TweetViewHolder(itemView: View) : ViewHolder(itemView) {

    fun updateWith(tweet: TweetViewModel, listener: (TweetLinkInfo) -> Unit) {
        val tweetView = itemView as TweetItemView
        tweetView.updateWith(tweet, listener)
    }
}
