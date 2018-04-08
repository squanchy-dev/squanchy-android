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
import net.squanchy.support.system.CurrentTime
import net.squanchy.support.text.applyTypeface
import net.squanchy.support.text.getFontFor
import net.squanchy.support.text.hasTypefaceSpan
import net.squanchy.support.unwrapToActivityContext
import timber.log.Timber

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
        service = component.scheduleService()
        navigate = component.navigator()
        analytics = component.analytics()
        viewPagerAdapter = ScheduleViewPagerAdapter(activity)
        currentTime = component.currentTime()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        tabstrip.setupWithViewPager(viewpager)
        hackToApplyTypefaces(tabstrip)

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
                R.id.action_filter -> {
                    navigate.toScheduleFiltering(context)
                    true
                }
                else -> false
            }
        }
    }

    override fun startLoading() {
        subscriptions.add(
            service.schedule()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { updateWith(it, ::onEventClicked) },
                    { Timber.e(it) }
                )
        )
    }

    private fun onEventClicked(event: Event) {
        analytics.trackItemSelected(ContentType.SCHEDULE_ITEM, event.id)
        navigate.toEventDetails(event.id)
    }

    override fun stopLoading() {
        subscriptions.clear()
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
                .mapNotNull(tabLayout::getTabAt)
                .filterNot { hasTypefaceSpan(it.text) }
                .forEach { it.text = it.text?.applyTypeface(typeface) }
        }
    }

    private fun hasTypefaceSpan(text: CharSequence?) = if (text !is Spanned) false else text.hasTypefaceSpan()

    fun updateWith(schedule: Schedule, onEventClicked: (Event) -> Unit) {
        progressbar.visibility = View.GONE

        if (schedule.isEmpty) {
            viewpager.visibility = GONE
            tabstrip.visibility = GONE
            emptyView.visibility = VISIBLE
            return
        }

        viewpager.visibility = VISIBLE
        tabstrip.visibility = VISIBLE
        emptyView.visibility = GONE

        viewPagerAdapter.updateWith(schedule.pages, onEventClicked)
        if (viewpager.adapter == null) {
            viewpager.adapter = viewPagerAdapter
        }

        progressbar.visibility = View.GONE
    }
}
