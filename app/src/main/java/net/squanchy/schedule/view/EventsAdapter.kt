package net.squanchy.schedule.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event

internal class EventsAdapter(context: Context) : ListAdapter<Event, EventViewHolder>(DiffCallback) {

    init {
        setHasStableIds(true)
    }

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var showRoom = false
    private var showFavorites = false
    private var eventClickListener: OnEventClickListener? = null

    fun updateWith(list: List<Event>, showRoom: Boolean, showFavorites: Boolean, listener: OnEventClickListener) {
        this.showRoom = showRoom
        this.eventClickListener = listener

        if (this.showFavorites != showFavorites) {
            // We want to refresh the favourite visualization too which is not part of the model and thus
            // would get ignored by the DiffCallback when comparing things. We go nuclear and trigger
            // a full dataset change notification.
            this.showFavorites = showFavorites
            notifyDataSetChanged()
        }
        super.submitList(list)
    }

    override fun getItemId(position: Int) = getItem(position).numericId

    override fun getItemViewType(position: Int): Int {
        val itemType = getItem(position).type
        return when (itemType) {
            Event.Type.KEYNOTE,
            Event.Type.TALK,
            Event.Type.WORKSHOP -> ItemViewType.TYPE_TALK.ordinal

            Event.Type.COFFEE_BREAK,
            Event.Type.LUNCH,
            Event.Type.OTHER,
            Event.Type.REGISTRATION,
            Event.Type.SOCIAL -> ItemViewType.TYPE_OTHER.ordinal
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
        holder.updateWith(getItem(position), showRoom, showFavorites, eventClickListener)
    }

    @Deprecated(
        message = "Use updateWith() instead",
        replaceWith = ReplaceWith("updateWith(list, showRoom, eventClickListener)"),
        level = DeprecationLevel.ERROR
    )
    override fun submitList(list: MutableList<Event>?) {
        throw UnsupportedOperationException("Use updateWith() instead")
    }
}

internal class EventViewHolder(itemView: EventItemView) : RecyclerView.ViewHolder(itemView) {

    fun updateWith(event: Event, showRoom: Boolean, showFavorites: Boolean, listener: OnEventClickListener?) {
        (itemView as EventItemView).updateWith(event, showRoom = showRoom, showFavorite = showFavorites)
        itemView.setOnClickListener { listener?.invoke(event) }
    }
}

private object DiffCallback : DiffUtil.ItemCallback<Event>() {

    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.numericId == newItem.numericId
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}

private enum class ItemViewType {
    TYPE_TALK,
    TYPE_OTHER
}

typealias OnEventClickListener = (Event) -> Unit
