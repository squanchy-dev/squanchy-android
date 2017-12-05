package net.squanchy.schedule.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event

internal class EventsAdapter(context: Context) : RecyclerView.Adapter<EventViewHolder>() {

    enum class ItemViewType {
        TYPE_TALK,
        TYPE_OTHER
    }

    private val layoutInflater: LayoutInflater
    private lateinit var listener: (Event) -> Unit
    private var _events = emptyList<Event>()
    val events get() = _events

    init {
        setHasStableIds(true)
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getItemId(position: Int) = _events[position].numericId

    fun updateWith(events: List<Event>, listener: (Event) -> Unit) {
        this._events = events
        this.listener = listener
    }

    override fun getItemViewType(position: Int): Int {
        val itemType = _events[position].type
        return when (itemType) {
            Event.Type.KEYNOTE, Event.Type.TALK -> ItemViewType.TYPE_TALK.ordinal
            Event.Type.COFFEE_BREAK, Event.Type.LUNCH, Event.Type.OTHER, Event.Type.REGISTRATION, Event.Type.SOCIAL -> ItemViewType.TYPE_OTHER.ordinal
            else -> throw IllegalArgumentException("Item of type $itemType is not supported")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemViewType = ItemViewType.values()[viewType]
        val itemView: EventItemView = when (itemViewType) {
            ItemViewType.TYPE_TALK -> layoutInflater.inflate(R.layout.item_schedule_event_talk, parent, false) as EventItemView
            ItemViewType.TYPE_OTHER -> layoutInflater.inflate(R.layout.item_schedule_event_other, parent, false) as EventItemView
            else -> throw IllegalArgumentException("View type not supported: $viewType")
        }
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.updateWith(_events[position], listener)
    }

    override fun getItemCount() = _events.size

}
