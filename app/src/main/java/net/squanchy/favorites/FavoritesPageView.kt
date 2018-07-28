package net.squanchy.favorites

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.merge_no_favorites_view.view.*
import kotlinx.android.synthetic.main.view_page_favorites.view.*
import net.squanchy.R
import net.squanchy.analytics.Analytics
import net.squanchy.analytics.ContentType
import net.squanchy.favorites.view.FavoritesItem
import net.squanchy.home.HomeActivity
import net.squanchy.home.Loadable
import net.squanchy.navigation.Navigator
import net.squanchy.remoteconfig.FeatureFlags
import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.unwrapToActivityContext
import timber.log.Timber

class FavoritesPageView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), Loadable {

    private lateinit var favoritesService: FavoritesService
    private lateinit var navigator: Navigator
    private lateinit var analytics: Analytics
    private lateinit var featureFlags: FeatureFlags

    private val disposable = CompositeDisposable()

    init {
        if (!isInEditMode) {
            with(favoritesComponent(context.unwrapToActivityContext())) {
                favoritesService = favoritesService()
                navigator = navigator()
                analytics = analytics()
                featureFlags = featureFlags()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        emptyViewSignedOutButton.setOnClickListener { showSignIn() }

        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.apply {
            title = resources.getString(R.string.activity_favorites)
            inflateMenu(R.menu.homepage)
            setOnMenuItemClickListener(this@FavoritesPageView::onMenuItemClickListener)
        }
    }

    override fun startLoading() {
        disposable.add(
            Observable.combineLatest(
                favoritesService.favorites(),
                favoritesService.currentUserIsSignedIn(),
                featureFlags.showEventRoomInSchedule.toObservable(),
                Function3<List<FavoritesItem>, Boolean, Boolean, LoadResult>(::LoadResult)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::handleLoadSchedule, Timber::e)
        )
    }

    override fun stopLoading() = disposable.clear()

    private fun onMenuItemClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_search -> {
                showSearch()
                true
            }
            R.id.action_settings -> {
                showSettings()
                true
            }
            else -> false
        }
    }

    private fun handleLoadSchedule(result: LoadResult) {
        when {
            result.favoriteItems.isNotEmpty() -> showFavorites(result.favoriteItems, result.showRoom)
            result.signedIn -> promptToFavorite()
            else -> promptToSign()
        }
    }

    private fun showFavorites(favorites: List<FavoritesItem>, showRoom: Boolean) {
        favoritesListView.updateWith(favorites, showRoom, ::navigateToEventDetails)
        favoritesListView.isVisible = true
        progressBar.isVisible = false
        emptyViewSignedOut.isVisible = false
        emptyViewSignedIn.isVisible = false
    }

    private fun navigateToEventDetails(event: Event) {
        analytics.trackItemSelected(ContentType.FAVORITES_ITEM, event.id)
        navigator.toEventDetails(event.id)
    }

    private fun promptToSign() {
        favoritesListView.isVisible = false
        progressBar.isVisible = false
        emptyViewSignedOut.isVisible = true
        emptyViewSignedIn.isVisible = false
    }

    private fun promptToFavorite() {
        favoritesListView.isVisible = false
        progressBar.isVisible = false
        emptyViewSignedOut.isVisible = false
        emptyViewSignedIn.isVisible = true
    }

    private fun showSignIn() {
        // ⚠️ HACK this is DIRTY and HORRIBLE but it's the only way we can ship this
        // without rewriting the whole data layer. Sorry. I swear, we know it sucks
        // and we want to fix this ASAP.
        val activity: HomeActivity = context.unwrapToActivityContext() as HomeActivity
        activity.requestSignIn()
    }

    private fun showSearch() = navigator.toSearch()

    private fun showSettings() = navigator.toSettings()

    private data class LoadResult(val favoriteItems: List<FavoritesItem>, val signedIn: Boolean, val showRoom: Boolean)
}
