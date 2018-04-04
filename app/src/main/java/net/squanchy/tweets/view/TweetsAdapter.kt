package net.squanchy.tweets.view

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import net.squanchy.R
import net.squanchy.tweets.domain.TweetLinkInfo
import net.squanchy.tweets.domain.view.TweetViewModel

class TweetsAdapter(
    context: Context,
    private val linkClickedListener: OnTweetLinkClicked
) : ListAdapter<TweetViewModel, TweetViewHolder>(DiffCallback) {

    init {
        setHasStableIds(true)
    }

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val view = inflater.inflate(R.layout.item_tweet, parent, false)
        return TweetViewHolder(view)
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.updateWith(getItem(position), linkClickedListener)
    }

    val isEmpty: Boolean
        get() = itemCount == 0

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }
}

private object DiffCallback : DiffUtil.ItemCallback<TweetViewModel>() {
    override fun areItemsTheSame(oldItem: TweetViewModel?, newItem: TweetViewModel?): Boolean {
        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItem: TweetViewModel?, newItem: TweetViewModel?): Boolean {
        return oldItem == newItem
    }
}

typealias OnTweetLinkClicked = (TweetLinkInfo) -> Unit
