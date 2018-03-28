package net.squanchy.schedule.view

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.view.CardSpacingItemDecorator

class ScheduleDayPageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private lateinit var adapter: EventsAdapter

    override fun onFinishInflate() {
        super.onFinishInflate()

        val layoutManager = SnappingLinearLayoutManager(context)
        setLayoutManager(layoutManager)
        adapter = EventsAdapter(context)
        setAdapter(adapter)

        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.card_horizontal_margin)
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.card_vertical_margin)
        addItemDecoration(CardSpacingItemDecorator(horizontalSpacing, verticalSpacing))
    }

    fun updateWith(newData: List<Event>, listener: (Event) -> Unit) {
        val callback = EventsDiffCallback(adapter.events, newData)
        val diffResult = DiffUtil.calculateDiff(callback, true) // TODO move off the UI thread
        adapter.updateWith(newData, listener)
        diffResult.dispatchUpdatesTo(adapter)
    }

    private var userHasScrolled: Boolean = false

    override fun onScrolled(dx: Int, dy: Int) {
        userHasScrolled = true
        super.onScrolled(dx, dy)
    }

    fun autoscrollToEvent(eventPosition: Int, animate: Boolean) {
        if (userHasScrolled) return // TODO only do it if it's an actual autoscroll (i.e., not because user has tapped the tab)

        when {
            animate -> smoothScrollToPosition(eventPosition)
            else -> scrollToPosition(eventPosition)
        }
    }

    private class EventsDiffCallback(
        private val oldEvents: List<Event>,
        private val newEvents: List<Event>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldEvents.size

        override fun getNewListSize() = newEvents.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldEvents[oldItemPosition].id == newEvents[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldEvents[oldItemPosition] == newEvents[newItemPosition]
    }

    private class SnappingLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

        override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
            val smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
                override fun getVerticalSnapPreference() = SNAP_TO_START
            }
            smoothScroller.targetPosition = position
            startSmoothScroll(smoothScroller)
        }
    }
}
