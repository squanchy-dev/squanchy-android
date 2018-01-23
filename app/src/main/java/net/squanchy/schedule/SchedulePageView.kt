package net.squanchy.schedule

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.TabLayout
import android.text.Spanned
import android.util.AttributeSet
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.view_page_schedule.view.*
import net.squanchy.R
import net.squanchy.analytics.Analytics
import net.squanchy.analytics.ContentType
import net.squanchy.home.Loadable
import net.squanchy.navigation.Navigator
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.domain.view.SchedulePage
import net.squanchy.schedule.view.ScheduleViewPagerAdapter
import net.squanchy.support.font.applyTypeface
import net.squanchy.support.font.getFontFor
import net.squanchy.support.font.hasTypefaceSpan
import net.squanchy.support.system.CurrentTime
import net.squanchy.support.unwrapToActivityContext
import timber.log.Timber

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class SchedulePageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr), Loadable {

    private val viewPagerAdapter: ScheduleViewPagerAdapter
    private val service: ScheduleService
    private val navigate: Navigator
    private val analytics: Analytics
    private val currentTime: CurrentTime
    private var subscriptions = CompositeDisposable()

    init {
        val activity = unwrapToActivityContext(getContext())
        val component = scheduleComponent(activity)
        service = component.service()
        navigate = component.navigator()
        analytics = component.analytics()
        viewPagerAdapter = ScheduleViewPagerAdapter(activity)
        currentTime = component.currentTime()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        tabstrip.setupWithViewPager(viewpager)
        hackToApplyTypefaces(tabstrip)

        viewpager.adapter = viewPagerAdapter

        tabstrip.addOnTabSelectedListener(TrackingOnTabSelectedListener(analytics, viewPagerAdapter))

        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.setTitle(R.string.activity_schedule)
        toolbar.inflateMenu(R.menu.homepage)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_search -> {
                    navigate.toSearch()
                    true
                }
                R.id.action_settings -> {
                    navigate.toSettings()
                    true
                }
                else -> false
            }
        }
    }

    override fun startLoading() {
        subscriptions.add(
                service.schedule(false)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { updateWith(it, { event -> onEventClicked(event) }) },
                            { Timber.e(it) }
                    )
        )
    }

    private fun onEventClicked(event: Event) {
        analytics.trackItemSelected(ContentType.SCHEDULE_ITEM, event.id)
        navigate.toEventDetails(event.id)
    }

    override fun stopLoading() {
        subscriptions.dispose()
    }

    private fun hackToApplyTypefaces(tabLayout: TabLayout) {
        // Unfortunately doing this the sensible way (in ScheduleViewPagerAdapter.getPageTitle())
        // results in a bunch of other views on screen to stop drawing their text, for reasons only
        // known to the Gods of Kobol. We can't theme things in the TabLayout either, as the
        // TextAppearance is applied _after_ inflating the tab views, which means Calligraphy can't
        // intercept that either. Sad panda.
        tabLayout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val context = tabLayout.context
            val typeface = context.getFontFor(R.style.TextAppearance_Squanchy_Tab)

            (0 until tabLayout.tabCount)
                .mapNotNull { tabLayout.getTabAt(it) }
                .filterNot { hasTypefaceSpan(it.text) }
                .forEach { it.text = it.text?.applyTypeface(typeface) }
        }
    }

    private fun hasTypefaceSpan(text: CharSequence?) = if (text !is Spanned) {
        false
    } else {
        text.hasTypefaceSpan()
    }

    fun updateWith(schedule: Schedule, onEventClicked: (Event) -> Unit) {
        val initialEventForPage = schedule.pages.map(::findNextEventForPage)
        viewPagerAdapter.updateWith(schedule.pages, initialEventForPage, onEventClicked)

        val todayPageIndex = findTodayIndexOrDefault(schedule.pages)
        viewpager.setCurrentItem(todayPageIndex, false)

        progressbar.visibility = View.GONE
    }

    private fun findTodayIndexOrDefault(pages: List<SchedulePage>): Int {
        val now = currentTime.currentLocalDateTime()
        return pages.firstOrNull { it.date.toLocalDate().isEqual(now.toLocalDate()) }
            ?.let(pages::indexOf) ?: 0
    }

    private fun findNextEventForPage(page: SchedulePage) = page.events
        .firstOrNull {
            it.startTime.toDateTime(it.timeZone).isAfter(currentTime.currentLocalDateTime().toDateTime(it.timeZone))
        }

    private class TrackingOnTabSelectedListener constructor(
            private val analytics: Analytics,
            private val viewPagerAdapter: ScheduleViewPagerAdapter
    ) : TabLayout.OnTabSelectedListener {

        override fun onTabSelected(tab: TabLayout.Tab) {
            analytics.trackItemSelected(ContentType.SCHEDULE_DAY, viewPagerAdapter.getPageDayId(tab.position))
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            // No-op
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            // No-op
        }
    }
}
