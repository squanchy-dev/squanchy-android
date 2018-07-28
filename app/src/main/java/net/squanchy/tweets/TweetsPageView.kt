package net.squanchy.tweets

import android.content.Context
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.view_page_tweets.view.*
import net.squanchy.R
import net.squanchy.home.Loadable
import net.squanchy.navigation.Navigator
import net.squanchy.support.unwrapToActivityContext
import net.squanchy.support.view.setAdapterIfNone
import net.squanchy.tweets.domain.TweetLinkInfo
import net.squanchy.tweets.domain.view.TwitterScreenViewModel
import net.squanchy.tweets.service.TwitterService
import net.squanchy.tweets.view.TweetsAdapter
import timber.log.Timber

class TweetsPageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr), Loadable {

    init {
        with(twitterComponent(context.unwrapToActivityContext())) {
            twitterService = service()
            navigator = navigator()
        }
    }

    private lateinit var twitterService: TwitterService
    private lateinit var navigator: Navigator

    private val tweetClickListener: ((TweetLinkInfo) -> Unit) = { navigator.toTweet(it) }
    private val tweetsAdapter = TweetsAdapter(context, tweetClickListener)

    private lateinit var subscription: Disposable

    override fun onFinishInflate() {
        super.onFinishInflate()

        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.inflateMenu(R.menu.homepage)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_search -> {
                    navigator.toSearch()
                    true
                }
                R.id.action_settings -> {
                    navigator.toSettings()
                    true
                }
                else -> false
            }
        }
    }

    override fun startLoading() {
        subscription = twitterService.refresh()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onTweetsFetched, ::onError)
    }

    override fun stopLoading() {
        subscription.dispose()
    }

    private fun onTweetsFetched(data: TwitterScreenViewModel) {
        tweetFeed.setAdapterIfNone(tweetsAdapter)

        toolbar.title = data.hashtag
        tweetsAdapter.submitList(data.tweets)
        updateUi()
    }

    private fun onError(e: Throwable) {
        Timber.e(e, "Error refreshing the Twitter timeline")
        updateUi()
    }

    private fun updateUi() {
        if (tweetsAdapter.isEmpty) {
            tweetEmptyView.isVisible = true
            tweetFeed.isVisible = false
        } else {
            tweetEmptyView.isVisible = false
            tweetFeed.isVisible = true
        }
    }
}
