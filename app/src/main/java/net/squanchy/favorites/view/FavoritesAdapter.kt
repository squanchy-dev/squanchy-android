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
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate

internal class FavoritesAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_TALK: Int = 1
        private const val VIEW_TYPE_HEADER: Int = 2
    }

    private val layoutInflater = LayoutInflater.from(context)

    private var schedule = Schedule.create(emptyList(), DateTimeZone.UTC)
    private var listener: ((Event) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = produceData(
        pages = schedule.pages,
        absolutePosition = position,
        headerProducer = { schedulePage -> (-schedulePage.dayId.hashCode()).toLong() },
        rowProducer = { schedulePage, positionInPage -> schedulePage.events[positionInPage].numericId }
    )

    fun updateWith(schedule: Schedule, listener: (Event) -> Unit) {
        this.schedule = schedule
        this.listener = listener
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = produceData(
        pages = schedule.pages,
        absolutePosition = position,
        headerProducer = { _ -> VIEW_TYPE_HEADER },
        rowProducer = { _, _ -> VIEW_TYPE_TALK }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TALK -> {
                val itemView = layoutInflater.inflate(R.layout.item_schedule_event_talk, parent, false) as EventItemView
                EventViewHolder(itemView)
            }
            VIEW_TYPE_HEADER -> {
                HeaderViewHolder(layoutInflater.inflate(R.layout.item_search_header, parent, false))
            }
            else -> throw IllegalArgumentException("View type not supported: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventViewHolder) {
            val event = produceData(
                pages = schedule.pages,
                absolutePosition = position,
                headerProducer = { throw IndexOutOfBoundsException() },
                rowProducer = { schedulePage, positionInPage -> schedulePage.events[positionInPage] }
            )
            if (listener != null) {
                holder.updateWith(event, listener!!)
            }
        } else if (holder is HeaderViewHolder) {
            val date = produceData(
                pages = schedule.pages,
                absolutePosition = position,
                headerProducer = { page -> page.date },
                rowProducer = { _, _ -> throw IndexOutOfBoundsException() }
            )
            holder.updateWith(formatHeader(date))
        }
    }

    private fun formatHeader(date: LocalDate): CharSequence = date.toString("EEEE d")

    override fun getItemCount(): Int = schedule.pages.fold(0) { count, page ->
        if (page.events.isNotEmpty()) {
            count + page.events.size + 1
        } else {
            count
        }
    }

    private fun <T> produceData(
        pages: List<SchedulePage>,
        pageIndex: Int = 0,
        absolutePosition: ItemPosition,
        headerProducer: (SchedulePage) -> T,
        rowProducer: (SchedulePage, Int) -> T
    ): T {
        if (pageIndex >= pages.size) {
            throw IndexOutOfBoundsException()
        }

        val schedulePage = pages[pageIndex]
        val pageEventsCount = schedulePage.events.size
        val nextPageIndex = pageIndex + 1
        var firstPositionInNextPage: ItemPosition = absolutePosition

        if (pageEventsCount != 0) {
            if (absolutePosition.isHeader()) {
                return headerProducer(schedulePage)
            }

            val adjustedPosition = absolutePosition - 1
            if (adjustedPosition.isRowWithin(pageEventsCount)) {
                return rowProducer(schedulePage, adjustedPosition)
            }

            firstPositionInNextPage = adjustedPosition - pageEventsCount
        }
        return produceData(pages, nextPageIndex, firstPositionInNextPage, headerProducer, rowProducer)
    }
}

private typealias ItemPosition = Int

private fun ItemPosition.isHeader() = this == 0

private fun ItemPosition.isRowWithin(eventsCount: Int) = this < eventsCount
