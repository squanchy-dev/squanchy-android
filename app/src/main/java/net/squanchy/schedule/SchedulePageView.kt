package net.squanchy.schedule

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import arrow.core.Tuple3
import com.google.android.material.tabs.TabLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_page_schedule.view.*
import net.squanchy.R
import net.squanchy.analytics.Analytics
import net.squanchy.analytics.ContentType
import net.squanchy.home.Loadable
import net.squanchy.navigation.Navigator
import net.squanchy.remoteconfig.FeatureFlags
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.view.ScheduleViewPagerAdapter
import net.squanchy.settings.preferences.showFavoritesInScheduleObservable
import net.squanchy.support.system.CurrentTime
import net.squanchy.support.text.applyTypeface
import net.squanchy.support.text.getFontFor
import net.squanchy.support.text.hasTypefaceSpan
import net.squanchy.support.unwrapToActivityContext
import net.squanchy.support.widget.calculateMenuItemCenterCoordinates
import timber.log.Timber

class SchedulePageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), Loadable {

    private val viewPagerAdapter: ScheduleViewPagerAdapter
    private val service: ScheduleService
    private val navigate: Navigator
    private val analytics: Analytics
    private val currentTime: CurrentTime
    private val featureFlags: FeatureFlags
    private var subscriptions = CompositeDisposable()

    init {
        val activity = context.unwrapToActivityContext()
        val component = scheduleComponent(activity)
        service = component.scheduleService()
        navigate = component.navigator()
        analytics = component.analytics()
        viewPagerAdapter = ScheduleViewPagerAdapter(activity)
        currentTime = component.currentTime()
        featureFlags = component.featureFlags()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        tabstrip.setupWithViewPager(viewPager)
        hackToApplyTypefaces(tabstrip)

        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.setTitle(R.string.activity_schedule)
        toolbar.inflateMenu(R.menu.homepage)
        toolbar.menu.findItem(R.id.action_filter).isVisible = true

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
                    navigate.toScheduleFiltering(toolbar.calculateMenuItemCenterCoordinates(R.id.action_filter))
                    true
                }
                else -> false
            }
        }
    }

    override fun startLoading() {
        subscriptions.add(
            Observable.combineLatest(
                service.schedule(),
                featureFlags.showEventRoomInSchedule.toObservable(),
                showFavoritesInScheduleObservable(context),
                Function3<Schedule, Boolean, Boolean, Tuple3<Schedule, Boolean, Boolean>>(::Tuple3)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { updateWith(it.a, it.b, it.c, ::onEventClicked) },
                    Timber::e
                )
        )
    }

    private fun updateWith(schedule: Schedule, showRoom: Boolean, showFavorites: Boolean, onEventClicked: (Event) -> Unit) {
        progressBar.isVisible = false

        if (schedule.isEmpty) {
            viewPager.isVisible = false
            tabstrip.isVisible = false
            emptyView.isVisible = true
            return
        }

        viewPager.isVisible = true
        tabstrip.isVisible = true
        emptyView.isVisible = false

        viewPagerAdapter.updateWith(schedule.pages, showRoom, showFavorites, onEventClicked)
        if (viewPager.adapter == null) {
            viewPager.adapter = viewPagerAdapter
        }

        progressBar.isVisible = false
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

    private fun hasTypefaceSpan(text: CharSequence?) = (text as? Spanned)?.hasTypefaceSpan() ?: false
}
