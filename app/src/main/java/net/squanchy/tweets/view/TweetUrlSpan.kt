package net.squanchy.tweets.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Browser
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

import timber.log.Timber

class TweetUrlSpan internal constructor(
        private val url: String,
        private val linkColor: Int
) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {
        ds.color = linkColor
    }

    override fun onClick(view: View) {
        val context = view.context
        val intent = createIntentWith(context)
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Timber.e(e, "Unable to start activity for Twitter url: %s", url)
        }
    }

    private fun createIntentWith(context: Context): Intent {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.packageName)
        return intent
    }
}
