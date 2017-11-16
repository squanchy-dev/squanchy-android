package net.squanchy.schedule.view

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.view.CardSpacingItemDecorator

class ScheduleDayPageView : RecyclerView {

    private lateinit var adapter: EventsAdapter

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onFinishInflate() {
        super.onFinishInflate()

        val layoutManager = LinearLayoutManager(context)
        setLayoutManager(layoutManager)
        adapter = EventsAdapter(context)
        setAdapter(adapter)

        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.card_horizontal_margin)
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.card_vertical_margin)
        addItemDecoration(CardSpacingItemDecorator(horizontalSpacing, verticalSpacing))
    }

    fun updateWith(newData: List<Event>, listener: ScheduleViewPagerAdapter.OnEventClickedListener) {
        val callback = EventsDiffCallback(adapter.events(), newData)
        val diffResult = DiffUtil.calculateDiff(callback, true)    // TODO move off the UI thread
        adapter.updateWith(newData, listener)
        diffResult.dispatchUpdatesTo(adapter)
    }

    private class EventsDiffCallback internal constructor(
            private val oldEvents: List<Event>,
            private val newEvents: List<Event>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldEvents.size

        override fun getNewListSize() = newEvents.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val (id) = oldEvents[oldItemPosition]
            val (id1) = newEvents[newItemPosition]
            return id == id1
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldEvent = oldEvents[oldItemPosition]
            val newEvent = newEvents[newItemPosition]
            return oldEvent == newEvent
        }
    }
}
