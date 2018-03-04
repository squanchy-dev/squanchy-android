package net.squanchy.tweets

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.view_page_tweets.view.*
import net.squanchy.R
import net.squanchy.home.Loadable
import net.squanchy.navigation.Navigator
import net.squanchy.support.unwrapToActivityContext
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

    private val component = twitterComponent(unwrapToActivityContext(context))
    private val twitterService: TwitterService = component.service()
    private val navigator: Navigator = component.navigator()

    private val tweetClickListener: ((TweetLinkInfo) -> Unit) = { navigator.toTweet(it) }

    private val tweetsAdapter = TweetsAdapter(context)

    private lateinit var subscription: Disposable

    override fun onFinishInflate() {
        super.onFinishInflate()

        setupToolbar()

        tweetFeed.adapter = tweetsAdapter
    }

    private fun setupToolbar() {
        toolbar.inflateMenu(R.menu.homepage)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_search -> { navigator.toSearch(); true }
                R.id.action_settings -> { navigator.toSettings(); true }
                else -> false
            }
        }
    }

    override fun startLoading() {
        subscription = twitterService.refresh()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onSuccess, ::onError)
    }

    override fun stopLoading() {
        subscription.dispose()
    }

    private fun onSuccess(data: TwitterScreenViewModel) {
        toolbar.title = data.hashtag
        tweetsAdapter.updateWith(data.tweets, tweetClickListener)
        onRefreshCompleted()
    }

    private fun onError(e: Throwable) {
        Timber.e(e, "Error refreshing the Twitter timeline")
        onRefreshCompleted()
    }

    private fun onRefreshCompleted() {
        if (tweetsAdapter.isEmpty) {
            tweetEmptyView.visibility = View.VISIBLE
            tweetFeed.visibility = View.GONE
        } else {
            tweetEmptyView.visibility = View.GONE
            tweetFeed.visibility = View.VISIBLE
        }
    }
}
