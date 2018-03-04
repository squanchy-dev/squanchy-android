package net.squanchy.tweets.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import net.squanchy.R
import net.squanchy.support.unwrapToActivityContext
import kotlinx.android.synthetic.main.item_tweet.view.tweetUserPhoto
import kotlinx.android.synthetic.main.item_tweet.view.tweetFooterText
import net.squanchy.imageloader.imageLoaderComponent

class TweetFooterView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val imageLoader = imageLoaderComponent(unwrapToActivityContext(context)).imageLoader()

    init {
        super.setOrientation(LinearLayout.HORIZONTAL)
    }

    override fun setOrientation(orientation: Int) {
        throw UnsupportedOperationException("Changing orientation is not supported for TweetFooterView")
    }

    fun updateWith(url: String, formattedRecap: String) {
        tweetFooterText.text = formattedRecap
        updateUserPhoto(url)
    }

    private fun updateUserPhoto(url: String) {
        tweetUserPhoto.setImageDrawable(null)
        imageLoader.load(url)
            .error(R.drawable.ic_no_avatar)
            .into(tweetUserPhoto)
    }
}
