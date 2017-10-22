package net.squanchy.tweets.view

import android.content.Context
import android.content.res.Resources
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.util.TypedValue

import net.squanchy.R

class TweetUrlSpanFactory(private val context: Context) {

    fun createFor(url: String): TweetUrlSpan {
        val linkColor = getColorFromTheme(context.theme, R.attr.tweetLinkTextColor)
        return TweetUrlSpan(url, linkColor)
    }

    @ColorInt
    private fun getColorFromTheme(theme: Resources.Theme, @AttrRes attributeId: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attributeId, typedValue, true)
        return typedValue.data
    }
}
