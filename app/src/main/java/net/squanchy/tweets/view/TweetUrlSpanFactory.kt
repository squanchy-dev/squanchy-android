package net.squanchy.tweets.view

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.TypedValue
import com.twitter.sdk.android.core.models.HashtagEntity
import com.twitter.sdk.android.core.models.MentionEntity
import com.twitter.sdk.android.core.models.UrlEntity

import net.squanchy.R
import net.squanchy.service.firestore.model.twitter.FirestoreTwitterHashtag
import net.squanchy.service.firestore.model.twitter.FirestoreTwitterMention
import net.squanchy.service.firestore.model.twitter.FirestoreTwitterUrl
import java.util.regex.Pattern

class TweetUrlSpanFactory(private val context: Context) {

    fun applySpansToTweet(
            text: String,
            startIndex: Int,
            hashtags: List<FirestoreTwitterHashtag>,
            mentions: List<FirestoreTwitterMention>,
            urls: List<FirestoreTwitterUrl>
    ): Spanned {
        val builder = SpannableStringBuilder(text)

        hashtags.forEach {
            val hashtag = offsetStart(it, startIndex)
            builder.setSpan(hashtag.createUrlSpanWith(this), hashtag.start, hashtag.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        mentions.forEach {
            val mention = offsetStart(it, startIndex)
            builder.setSpan(mention.createUrlSpanWith(this), mention.start, mention.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        urls.forEach {
            val url = offsetStart(it, startIndex)
            builder.setSpan(url.createUrlSpanWith(this), url.start, url.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        unescapeEntities(builder)
        return builder
    }

    private fun offsetStart(entity: FirestoreTwitterHashtag, startIndex: Int): FirestoreTwitterHashtag {
        entity.start = entity.start - startIndex
        entity.end = entity.end - startIndex
        return entity
    }

    private fun FirestoreTwitterHashtag.createUrlSpanWith(spanFactory: TweetUrlSpanFactory): TweetUrlSpan =
        spanFactory.createFor(String.format(QUERY_URL_TEMPLATE, text))

    private fun createFor(url: String): TweetUrlSpan {
        val linkColor = getColorFromTheme(context.theme, R.attr.tweetLinkTextColor)
        return TweetUrlSpan(url, linkColor)
    }

    @ColorInt
    private fun getColorFromTheme(theme: Resources.Theme, @AttrRes attributeId: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attributeId, typedValue, true)
        return typedValue.data
    }

    private fun offsetStart(entity: FirestoreTwitterMention, startIndex: Int): FirestoreTwitterMention {
        entity.start = entity.start - startIndex
        entity.end = entity.end - startIndex
        return entity
    }

    private fun FirestoreTwitterMention.createUrlSpanWith(spanFactory: TweetUrlSpanFactory): TweetUrlSpan =
        spanFactory.createFor(String.format(MENTION_URL_TEMPLATE, screenName))

    private fun offsetStart(entity: FirestoreTwitterUrl, startIndex: Int): FirestoreTwitterUrl {
        entity.start = entity.start - startIndex
        entity.end = entity.end - startIndex
        return entity
    }

    private fun FirestoreTwitterUrl.createUrlSpanWith(spanFactory: TweetUrlSpanFactory): TweetUrlSpan =
        spanFactory.createFor(url)

    private fun unescapeEntities(builder: SpannableStringBuilder) {
        val string = builder.toString()
        val matcher = HTML_ENTITY_PATTERN.matcher(string)

        if (matcher.find()) {
            val matchResult = matcher.toMatchResult()
            val unescapedEntity = parseHtml(matchResult.group())
            builder.replace(matchResult.start(), matchResult.end(), unescapedEntity)
            unescapeEntities(builder)
        }
    }

    @TargetApi(Build.VERSION_CODES.N) // The older fromHtml() is only called pre-24
    private fun parseHtml(description: String): Spanned {
        // TODO handle this properly
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION") // This is a "compat" method call, we only use this on pre-N
            Html.fromHtml(description)
        }
    }

    companion object {
        private const val BASE_TWITTER_URL = "https://twitter.com/"
        private const val MENTION_URL_TEMPLATE = BASE_TWITTER_URL + "%s"
        private const val QUERY_URL_TEMPLATE = BASE_TWITTER_URL + "search?q=%s"

        private val HTML_ENTITY_PATTERN = Pattern.compile("&#?\\w+;")
    }
}
