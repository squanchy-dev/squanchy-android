package net.squanchy.schedule.view

import android.support.v7.widget.RecyclerView

import net.squanchy.schedule.domain.view.Event

class EventViewHolder(itemView: EventItemView) : RecyclerView.ViewHolder(itemView) {

    fun updateWith(event: Event, listener: (Event) -> Unit) {
        (itemView as EventItemView).updateWith(event)
        itemView.setOnClickListener { listener.invoke(event) }
    }
}
