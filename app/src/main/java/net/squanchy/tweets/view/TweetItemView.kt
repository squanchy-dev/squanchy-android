package net.squanchy.tweets.view

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.item_tweet.view.*
import net.squanchy.R
import net.squanchy.imageloader.ImageLoader
import net.squanchy.imageloader.imageLoaderComponent
import net.squanchy.support.unwrapToActivityContext
import net.squanchy.support.widget.CardLayout
import net.squanchy.tweets.domain.TweetLinkInfo
import net.squanchy.tweets.domain.view.TweetViewModel

class TweetItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.attr.cardViewDefaultStyle
) : CardLayout(context, attrs, defStyleAttr) {

    private var imageLoader: ImageLoader? = null

    init {
        if (!isInEditMode) {
            val activity = context.unwrapToActivityContext()
            imageLoader = imageLoaderComponent(activity).imageLoader()
        }
    }

    private val footerFormatter: TwitterFooterFormatter = TwitterFooterFormatter(context)

    override fun onFinishInflate() {
        super.onFinishInflate()

        tweetText.movementMethod = LinkMovementMethod.getInstance()
    }

    fun updateWith(tweet: TweetViewModel, listener: (TweetLinkInfo) -> Unit) {
        tweetText.text = tweet.spannedText
        updatePhotoWith(tweet.photoUrl, imageLoader)
        tweetFooter.updateWith(tweet.user.photoUrl, footerFormatter.footerTextFor(tweet))

        setOnClickListener { listener(tweet.linkInfo) }
    }

    private fun updatePhotoWith(photoUrl: String?, imageLoader: ImageLoader?) {
        if (imageLoader == null) {
            throw IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet")
        }

        tweetPhoto.setImageDrawable(null)
        if (photoUrl != null) {
            tweetPhoto.isVisible = true
            imageLoader.load(photoUrl).into(tweetPhoto)
        } else {
            tweetPhoto.isVisible = false
        }
    }
}
