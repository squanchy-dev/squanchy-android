package net.squanchy.tweets.view

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.View
import net.squanchy.R
import net.squanchy.imageloader.ImageLoader
import net.squanchy.imageloader.ImageLoaderInjector
import net.squanchy.support.unwrapToActivityContext
import net.squanchy.support.widget.CardLayout
import net.squanchy.tweets.domain.TweetLinkInfo
import net.squanchy.tweets.domain.view.TweetViewModel
import kotlinx.android.synthetic.main.item_tweet.view.tweetPhoto
import kotlinx.android.synthetic.main.item_tweet.view.tweetFooter
import kotlinx.android.synthetic.main.item_tweet.view.tweetText

class TweetItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = R.attr.cardViewDefaultStyle
) : CardLayout(context, attrs, defStyleAttr) {

    private val component = ImageLoaderInjector.obtain(unwrapToActivityContext(context))
    private val imageLoader: ImageLoader = component.imageLoader()

    private val footerFormatter: TwitterFooterFormatter = TwitterFooterFormatter(context)

    override fun onFinishInflate() {
        super.onFinishInflate()

        tweetText.movementMethod = LinkMovementMethod.getInstance()
    }

    fun updateWith(tweet: TweetViewModel, listener: (TweetLinkInfo) -> Unit) {
        tweetText.text = tweet.spannedText
        updatePhotoWith(tweet.photoUrl)
        tweetFooter.updateWith(tweet.user.photoUrl, footerFormatter.footerTextFor(tweet))

        setOnClickListener { listener(tweet.linkInfo) }
    }

    private fun updatePhotoWith(photoUrl: String?) {
        tweetPhoto.setImageDrawable(null)
        if (photoUrl != null) {
            tweetPhoto.visibility = View.VISIBLE
            imageLoader.load(photoUrl).into(tweetPhoto)
        } else {
            tweetPhoto.visibility = View.GONE
        }
    }
}
