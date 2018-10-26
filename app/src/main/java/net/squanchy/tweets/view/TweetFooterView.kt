package net.squanchy.tweets.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.item_tweet.view.*
import net.squanchy.R
import net.squanchy.imageloader.ImageLoader
import net.squanchy.imageloader.imageLoaderComponent
import net.squanchy.support.unwrapToActivityContext

class TweetFooterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var imageLoader: ImageLoader? = null

    init {
        if (!isInEditMode) {
            val activity = context.unwrapToActivityContext()
            imageLoader = imageLoaderComponent(activity).imageLoader()
        }
        super.setOrientation(LinearLayout.HORIZONTAL)
    }

    override fun setOrientation(orientation: Int) {
        throw UnsupportedOperationException("Changing orientation is not supported for TweetFooterView")
    }

    fun updateWith(url: String, formattedRecap: String) {
        tweetFooterText.text = formattedRecap
        updateUserPhoto(url, imageLoader)
    }

    private fun updateUserPhoto(url: String, imageLoader: ImageLoader?) {
        if (imageLoader == null) {
            throw IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet")
        }

        tweetUserPhoto.setImageDrawable(null)
        imageLoader.load(url)
            .error(R.drawable.ic_no_avatar)
            .circleCrop()
            .into(tweetUserPhoto)
    }
}
