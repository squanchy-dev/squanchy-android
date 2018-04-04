package net.squanchy.schedule.view

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import net.squanchy.R
import net.squanchy.favorites.view.EventViewHolder
import net.squanchy.schedule.domain.view.Event

internal class EventsAdapter(
    context: Context
) : ListAdapter<Event, EventViewHolder>(DiffCallback) {

    init {
        setHasStableIds(true)
    }

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    lateinit var eventClickListener: OnEventClickListener

    override fun getItemId(position: Int) = getItem(position).numericId

    override fun getItemViewType(position: Int): Int {
        val itemType = getItem(position).type
        return when (itemType) {
            Event.Type.KEYNOTE, Event.Type.TALK, Event.Type.WORKSHOP -> ItemViewType.TYPE_TALK.ordinal
            Event.Type.COFFEE_BREAK, Event.Type.LUNCH, Event.Type.OTHER, Event.Type.REGISTRATION, Event.Type.SOCIAL -> ItemViewType.TYPE_OTHER.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemViewType = ItemViewType.values()[viewType]
        val itemView: EventItemView = when (itemViewType) {
            ItemViewType.TYPE_TALK -> layoutInflater.inflate(R.layout.item_schedule_event_talk, parent, false) as EventItemView
            ItemViewType.TYPE_OTHER -> layoutInflater.inflate(R.layout.item_schedule_event_other, parent, false) as EventItemView
        }
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.updateWith(getItem(position), eventClickListener)
    }
}

private object DiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event?, newItem: Event?): Boolean {
        return oldItem?.numericId == newItem?.numericId
    }

    override fun areContentsTheSame(oldItem: Event?, newItem: Event?): Boolean {
        return oldItem == newItem
    }
}

private enum class ItemViewType {
    TYPE_TALK,
    TYPE_OTHER
}

typealias OnEventClickListener = (Event) -> Unit
