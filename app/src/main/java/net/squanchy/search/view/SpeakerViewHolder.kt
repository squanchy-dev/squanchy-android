package net.squanchy.search.view

import android.support.v7.widget.RecyclerView
import android.view.View

import net.squanchy.imageloader.ImageLoader
import net.squanchy.search.SearchItemView
import net.squanchy.speaker.domain.view.Speaker

internal class SpeakerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun updateWith(speaker: Speaker, imageLoader: ImageLoader, listener: SearchRecyclerView.OnSearchResultClickListener) {
        (itemView as SearchItemView).updateWith(speaker, imageLoader, listener)
    }
}
