package net.squanchy.schedule.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.novoda.viewpageradapter.ViewPagerAdapter
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.SchedulePage
import java.util.Locale

class ScheduleViewPagerAdapter(context: Context) : ViewPagerAdapter<ScheduleDayPageView>() {

    private lateinit var listener: (Event) -> Unit

    private var pages = emptyList<SchedulePage>()
    private var initialEventForPage = emptyArray<Event?>()
    private var triggerScrollForPage = emptyArray<((Event) -> Unit)?>()

    private val inflater = LayoutInflater.from(context)

    fun updateWith(pages: List<SchedulePage>, initialEventForPage: Array<Event?>, listener: (Event) -> Unit) {
        this.pages = pages
        this.initialEventForPage = initialEventForPage
        this.listener = listener
        this.triggerScrollForPage = arrayOfNulls(pages.size)
        notifyDataSetChanged()
    }

    override fun getCount() = pages.size

    override fun createView(container: ViewGroup, position: Int): ScheduleDayPageView {
        return inflater.inflate(R.layout.view_page_schedule_day, container, false) as ScheduleDayPageView
    }

    override fun bindView(view: ScheduleDayPageView, position: Int) {
        val events = pages[position].events
        val initialEvent = initialEventForPage[position]
        triggerScrollForPage[position] = { view.autoscrollToEvent(events.indexOf(it), true) }
        view.updateWith(events, listener)
        initialEvent?.let { view.autoscrollToEvent(events.indexOf(it), false) }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val date = pages[position].date
        return date.toString(TITLE_FORMAT_TEMPLATE).toUpperCase(Locale.getDefault())
    }

    fun getPageDayId(position: Int) = pages[position].dayId

    fun refresh(page: Int, event: Event) {
        triggerScrollForPage[page]?.invoke(event)
    }

    override fun isViewFromObject(view: View, anObject: Any) = view === anObject

    companion object {
        private const val TITLE_FORMAT_TEMPLATE = "EEE d"
    }
}
