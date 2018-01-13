package net.squanchy.favorites

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.view_page_favorites.view.*
import net.squanchy.R
import net.squanchy.analytics.Analytics
import net.squanchy.analytics.ContentType
import net.squanchy.home.HomeActivity
import net.squanchy.home.Loadable
import net.squanchy.navigation.Navigator
import net.squanchy.schedule.ScheduleService
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.support.unwrapToActivityContext

class FavoritesPageView @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr), Loadable {

    private val favoritesComponent = favoritesComponent(unwrapToActivityContext(context))
    private val service: ScheduleService = favoritesComponent.service()
    private val navigator: Navigator = favoritesComponent.navigator()
    private val analytics: Analytics = favoritesComponent.analytics()
    private val disposable = CompositeDisposable()

    override fun onFinishInflate() {
        super.onFinishInflate()

        emptyViewSignedOutButton.setOnClickListener { showSignIn() }

        setupToolbar()
    }

    private fun setupToolbar() {
        with(toolbar) {
            title = resources.getString(R.string.activity_favorites)
            inflateMenu(R.menu.homepage)
            setOnMenuItemClickListener(this@FavoritesPageView::onMenuItemClickListener)
        }
    }

    override fun startLoading() {
        disposable.add(
                Observable.combineLatest(service.schedule(true), service.currentUserIsSignedIn(),
                BiFunction<Schedule, Boolean, LoadScheduleResult> { schedule, signedIn -> LoadScheduleResult(schedule, signedIn) })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { handleLoadSchedule(it) })
    }

    override fun stopLoading() = disposable.clear()

    private fun onMenuItemClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_search -> { showSearch(); true }
            R.id.action_settings -> { showSettings(); true }
            else -> false
        }
    }

    private fun handleLoadSchedule(result: LoadScheduleResult) {
        when {
            hasFavorites(result.schedule) -> showSchedule(result.schedule)
            result.signedIn -> promptToFavorite()
            else -> promptToSign()
        }
    }

    private fun hasFavorites(schedule: Schedule) = !schedule.isEmpty

    private fun showSchedule(schedule: Schedule) {
        favoritesListView.updateWith(schedule, this::showEventDetails)
        favoritesListView.visibility = View.VISIBLE
        emptyViewSignedIn.visibility = View.GONE
    }

    private fun showEventDetails(event: Event) {
        analytics.trackItemSelected(ContentType.FAVORITES_ITEM, event.id)
        navigator.toEventDetails(event.id)
    }

    private fun promptToSign() {
        emptyViewSignedOut.visibility = View.VISIBLE
        favoritesListView.visibility = View.GONE
        progressBar.visibility = View.GONE
        emptyViewSignedIn.visibility = View.GONE
    }

    private fun promptToFavorite() {
        emptyViewSignedIn.visibility = View.VISIBLE
        favoritesListView.visibility = View.GONE
        progressBar.visibility = View.GONE
        emptyViewSignedOut.visibility = View.GONE
    }

    private fun showSignIn() {
        // ⚠️ HACK this is DIRTY and HORRIBLE but it's the only way we can ship this
        // without rewriting the whole data layer. Sorry. I swear, we know it sucks
        // and we want to fix this ASAP.
        val activity: HomeActivity = unwrapToActivityContext(context) as HomeActivity
        activity.requestSignIn()
    }

    private fun showSearch() = navigator.toSearch()

    private fun showSettings() = navigator.toSettings()

    private data class LoadScheduleResult(val schedule: Schedule, val signedIn: Boolean)
}
