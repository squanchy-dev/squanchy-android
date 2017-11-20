package net.squanchy.favorites

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import net.squanchy.R
import net.squanchy.analytics.Analytics
import net.squanchy.analytics.ContentType
import net.squanchy.favorites.view.FavoritesListView
import net.squanchy.home.HomeActivity
import net.squanchy.home.Loadable
import net.squanchy.navigation.Navigator
import net.squanchy.schedule.ScheduleService
import net.squanchy.support.unwrapToActivityContext

class FavoritesPageView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        CoordinatorLayout(context, attrs, defStyleAttr), Loadable {

    private val favoritesComponent = favoritesComponent(unwrapToActivityContext(context))

    private val service: ScheduleService by lazy { favoritesComponent.service() }

    private val navigator: Navigator by lazy { favoritesComponent.navigator() }

    private val analytics: Analytics by lazy { favoritesComponent.analytics() }

    private lateinit var progressBar: ProgressBar

    private lateinit var favoritesListView: FavoritesListView

    private lateinit var emptyViewSignedIn: View

    private lateinit var emptyViewSignedOut: View

    private val actions = PublishSubject.create<FavoritesPageViewActions>()

    val disposable = CompositeDisposable()

    override fun onFinishInflate() {
        super.onFinishInflate()

        progressBar = findViewById(R.id.progressbar)
        favoritesListView = findViewById(R.id.favorites_list)
        emptyViewSignedIn = findViewById(R.id.empty_view_signed_in)
        emptyViewSignedOut = findViewById(R.id.empty_view_signed_out)

        findViewById<View>(R.id.empty_view_signed_out_button).setOnClickListener { requestSignIn() }

        setupToolbar()

        val viewState = favoritesPageViewPresenter(actions)

        disposable.add(viewState.observeOn(AndroidSchedulers.mainThread()).subscribe { uiState ->

            when (uiState) {

                is FavoritesPageViewState.ShowSchedule -> showSchedule(uiState)

                is FavoritesPageViewState.ShowEventDetails -> showEventDetails(uiState)

                is FavoritesPageViewState.PromptToSign -> promptToSign()

                is FavoritesPageViewState.PromptToFavorite -> promptToFavorite()

                is FavoritesPageViewState.RequestSignIn -> showSignIn()

                is FavoritesPageViewState.ShowSearch -> showSearch()

                is FavoritesPageViewState.ShowSettings -> showSettings()

                is FavoritesPageViewState.Idle -> Unit//do nothing
            }
        })
    }

    override fun startLoading() = actions.onNext(FavoritesPageViewActions.LoadSchedule(true, service))

    override fun stopLoading() = disposable.clear()

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        with(toolbar) {
            title = resources.getString(R.string.activity_favorites)
            inflateMenu(R.menu.homepage)
            setOnMenuItemClickListener(this@FavoritesPageView::onMenuItemClickListener)
        }
    }

    private fun onMenuItemClickListener(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {
            R.id.action_search -> { actions.onNext(FavoritesPageViewActions.SearchClicked); return true }
            R.id.action_settings -> { actions.onNext(FavoritesPageViewActions.SettingsClicked); return true }
        }

        return false
    }

    private fun requestSignIn() = actions.onNext(FavoritesPageViewActions.RequestSignIn)

    private fun showSchedule(uiState: FavoritesPageViewState.ShowSchedule) {
        favoritesListView.updateWith(uiState.schedule) { actions.onNext(FavoritesPageViewActions.EventClicked(it))}
        favoritesListView.visibility = View.VISIBLE
        emptyViewSignedIn.visibility = View.GONE
    }

    private fun showEventDetails(uiState: FavoritesPageViewState.ShowEventDetails) {
        analytics.trackItemSelected(ContentType.FAVORITES_ITEM, uiState.event.id)
        navigator.toEventDetails(uiState.event.id)
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
        val activity : HomeActivity = unwrapToActivityContext(context) as HomeActivity
        activity.requestSignIn()
    }

    private fun showSearch() = navigator.toSearch()

    private fun showSettings() = navigator.toSettings()

}