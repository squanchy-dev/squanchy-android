package net.squanchy.favorites

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.MenuItem
import androidx.view.isVisible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.view_page_favorites.view.*
import net.squanchy.R
import net.squanchy.analytics.Analytics
import net.squanchy.analytics.ContentType
import net.squanchy.favorites.view.FavoritesItem
import net.squanchy.home.HomeActivity
import net.squanchy.home.Loadable
import net.squanchy.navigation.Navigator
import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.unwrapToActivityContext

class FavoritesPageView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr), Loadable {

    private lateinit var favoritesService: FavoritesService
    private lateinit var navigator: Navigator
    private lateinit var analytics: Analytics

    private val disposable = CompositeDisposable()

    init {
        with(favoritesComponent(context.unwrapToActivityContext())) {
            favoritesService = favoritesService()
            navigator = navigator()
            analytics = analytics()
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
                BiFunction<List<FavoritesItem>, Boolean, LoadResult>(::LoadResult)
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::handleLoadSchedule)
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
            result.favoriteItems.isNotEmpty() -> showFavorites(result.favoriteItems)
            result.signedIn -> promptToFavorite()
            else -> promptToSign()
        }
    }

    private fun showFavorites(favorites: List<FavoritesItem>) {
        favoritesListView.updateWith(favorites, ::navigateToEventDetails)
        favoritesListView.isVisible = true
        emptyViewSignedIn.isVisible = false
        emptyViewSignedOut.isVisible = false
    }

    private fun navigateToEventDetails(event: Event) {
        analytics.trackItemSelected(ContentType.FAVORITES_ITEM, event.id)
        navigator.toEventDetails(event.id)
    }

    private fun promptToSign() {
        emptyViewSignedOut.isVisible = true
        favoritesListView.isVisible = false
        progressBar.isVisible = false
        emptyViewSignedIn.isVisible = false
    }

    private fun promptToFavorite() {
        emptyViewSignedIn.isVisible = true
        favoritesListView.isVisible = false
        progressBar.isVisible = false
        emptyViewSignedOut.isVisible = false
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

    private data class LoadResult(val favoriteItems: List<FavoritesItem>, val signedIn: Boolean)
}
