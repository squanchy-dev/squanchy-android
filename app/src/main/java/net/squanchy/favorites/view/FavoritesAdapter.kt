package net.squanchy.favorites.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.schedule.view.EventItemView
import net.squanchy.schedule.view.EventViewHolder
import net.squanchy.search.view.HeaderViewHolder
import net.squanchy.support.lang.Lists

import org.joda.time.LocalDateTime

// todo #333 too complicated logic. Need to refactor once sample data is available.
internal class FavoritesAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewTypeTalk: Int = 1
    private val viewTypeHeader: Int = 2

    private val layoutInflater: LayoutInflater

    private var schedule = Schedule.create(emptyList())

    private var listener: ((Event) -> Unit)? = null

    init {
        setHasStableIds(true)
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getItemId(position: Int): Long {
        return findFor(
                0,
                position,
                schedule.pages,
                { schedulePage -> (-schedulePage.dayId.hashCode()).toLong() },
                { schedulePage, positionInPage -> schedulePage.events[positionInPage].numericId }
        )
    }

    fun updateWith(schedule: Schedule, listener: (Event) -> Unit) {
        this.schedule = schedule
        this.listener = listener
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return findFor(
                0,
                position,
                schedule.pages,
                { _ -> viewTypeHeader },
                { _, _ -> viewTypeTalk }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == viewTypeTalk) {
            val itemView = layoutInflater.inflate(R.layout.item_schedule_event_talk, parent, false) as EventItemView
            return EventViewHolder(itemView)
        } else return if (viewType == viewTypeHeader) {
            HeaderViewHolder(layoutInflater.inflate(R.layout.item_search_header, parent, false))
        } else {
            throw IllegalArgumentException("View type not supported: " + viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventViewHolder) {
            val event = findFor<Event>(
                    0,
                    position,
                    schedule.pages,
                    { _ -> throw IndexOutOfBoundsException() },
                    { schedulePage, positionInPage -> schedulePage.events[positionInPage] }
            )

            holder.updateWith(event, listener)
        } else if (holder is HeaderViewHolder) {
            val date = findFor<LocalDateTime>(
                    0,
                    position,
                    schedule.pages,
                    { schedule.pages[position].date },
                    { _, _ -> throw IndexOutOfBoundsException() }
            )
            holder.updateWith(formatHeader(date))
        }
    }

    private fun formatHeader(date: LocalDateTime): CharSequence {
        return date.toString("EEEE d")
    }

    override fun getItemCount(): Int {
        return Lists.reduce(0, schedule.pages) { count, (_, _, events) -> count!! + events.size + 1 }
    }

    private fun <T> findFor(pagePosition: Int, position: Int, pages: List<SchedulePage>,
            header: (SchedulePage) -> T, row: (SchedulePage, Int) -> T): T {

        if (pagePosition >= pages.size) {
            throw IndexOutOfBoundsException()
        }

        val schedulePage = pages[pagePosition]

        if (position == 0) {
            return header(schedulePage)
        }

        val adjustedPosition = position - 1

        val size = schedulePage.events.size

        if (adjustedPosition < size) {
            return row(schedulePage, adjustedPosition)
        }

        return findFor(pagePosition + 1, adjustedPosition - size, pages, header, row)

    }

}

