package net.squanchy.schedule.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.novoda.viewpageradapter.ViewPagerAdapter
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.SchedulePage
import java.util.Locale

class ScheduleViewPagerAdapter(context: Context) : ViewPagerAdapter<ScheduleDayPageView>() {

    private lateinit var listener: (Event) -> Unit

    private var pages = emptyList<SchedulePage>()

    private val inflater = LayoutInflater.from(context)
    private val viewPool = RecyclerView.RecycledViewPool()
    private var showRoom: Boolean = false
    private var showFavorites: Boolean = false

    fun updateWith(pages: List<SchedulePage>, showRoom: Boolean, showFavorites: Boolean, listener: (Event) -> Unit) {
        this.pages = pages
        this.showRoom = showRoom
        this.showFavorites = showFavorites
        this.listener = listener
        notifyDataSetChanged()
    }

    override fun getCount() = pages.size

    override fun createView(container: ViewGroup, position: Int): ScheduleDayPageView {
        val recyclerView = inflater.inflate(R.layout.view_page_schedule_day, container, false) as ScheduleDayPageView
        recyclerView.setRecycledViewPool(viewPool)
        return recyclerView
    }

    override fun bindView(view: ScheduleDayPageView, position: Int) {
        val events = pages[position].events
        view.updateWith(events, showRoom, showFavorites, listener)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val date = pages[position].date
        return date.toString(TITLE_FORMAT_TEMPLATE).toUpperCase(Locale.getDefault())
    }

    override fun isViewFromObject(view: View, anObject: Any) = view === anObject

    companion object {
        private const val TITLE_FORMAT_TEMPLATE = "EEE d"
    }
}
