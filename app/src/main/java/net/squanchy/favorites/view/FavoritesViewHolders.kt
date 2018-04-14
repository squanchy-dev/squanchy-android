package net.squanchy.favorites.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.view.EventItemView
import net.squanchy.schedule.view.OnEventClickListener
import org.joda.time.LocalDate

fun favoriteItemViewHolderFor(itemView: View) = when (itemView) {
    is EventItemView -> EventViewHolder(itemView)
    is TextView -> HeaderViewHolder(itemView)
    else -> throw UnsupportedOperationException("View of type ${itemView.javaClass.name} is not supported!")
}

sealed class FavoritesViewHolder<T : View>(itemView: T) : RecyclerView.ViewHolder(itemView)

class EventViewHolder(itemView: EventItemView) : FavoritesViewHolder<EventItemView>(itemView) {

    fun updateWith(event: Event, listener: OnEventClickListener?) {
        (itemView as EventItemView).updateWith(event, showRoom = true, showFavorite = false)
        itemView.setOnClickListener { listener?.invoke(event) }
    }
}

class HeaderViewHolder(itemView: TextView) : FavoritesViewHolder<TextView>(itemView) {

    fun updateWith(date: LocalDate) {
        (itemView as TextView).text = date.toString("EEEE d")
    }
}
