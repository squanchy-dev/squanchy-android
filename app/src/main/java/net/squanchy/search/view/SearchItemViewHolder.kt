package net.squanchy.search.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import net.squanchy.imageloader.ImageLoader
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.view.EventItemView
import net.squanchy.search.SearchItemView
import net.squanchy.speaker.domain.view.Speaker

sealed class SearchItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    class SpeakerViewHolder(itemView: View) : SearchItemViewHolder(itemView) {

        fun updateWith(speaker: Speaker, imageLoader: ImageLoader, listener: SearchRecyclerView.OnSearchResultClickListener) {
            (itemView as SearchItemView).updateWith(speaker, imageLoader, listener)
        }
    }

    class HeaderViewHolder(itemView: View) : SearchItemViewHolder(itemView) {

        fun updateWith(headerType: HeaderType) {
            (itemView as TextView).setText(headerType.headerTextResourceId)
        }

        fun updateWith(label: CharSequence) {
            (itemView as TextView).text = label
        }
    }

    class AlgoliaLogoViewHolder(itemView: View) : SearchItemViewHolder(itemView)

    class SearchEventViewHolder(itemView: EventItemView) : SearchItemViewHolder(itemView) {

        fun updateWith(event: Event, listener: (Event) -> Unit) {
            (itemView as EventItemView).updateWith(event)
            itemView.setOnClickListener { listener(event) }
        }
    }
}