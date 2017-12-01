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
import net.squanchy.schedule.view.ScheduleViewPagerAdapter
import net.squanchy.support.font.applyTypeface
import net.squanchy.support.font.getFontFor
import net.squanchy.support.font.hasTypefaceSpan
import net.squanchy.support.unwrapToActivityContext
import timber.log.Timber


class SchedulePageView : CoordinatorLayout, Loadable {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        val activity = unwrapToActivityContext(getContext())
        val component = obtain(activity)
        service = component.service()
        navigate = component.navigator()
        analytics = component.analytics()

        viewPagerAdapter = ScheduleViewPagerAdapter(activity)
    }

    private val viewPagerAdapter: ScheduleViewPagerAdapter
    private val service: ScheduleService
    private val navigate: Navigator
    private val analytics: Analytics
    private var subscriptions = CompositeDisposable()

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
        tabLayout.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val context = tabLayout.context
            val typeface = context.getFontFor(R.style.TextAppearance_Squanchy_Tab)

            val tabCount = tabLayout.tabCount
            for (i in 0 until tabCount) {
                val tab = tabLayout.getTabAt(i)
                if (tab == null || hasTypefaceSpan(tab.text)) {
                    continue
                }
                tab.text = tab.text?.applyTypeface(typeface)
            }
        }
    }

    private fun hasTypefaceSpan(text: CharSequence?) = if (text !is Spanned) {
        false
    } else {
        text.hasTypefaceSpan()
    }

    fun updateWith(schedule: Schedule, onEventClicked: (Event) -> Unit) {
        viewPagerAdapter.updateWith(schedule.pages, onEventClicked)
        progressbar.visibility = View.GONE
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
