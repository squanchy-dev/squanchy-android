package net.squanchy.tweets.view

import android.content.Context
import android.content.res.Resources
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.TypedValue
import net.squanchy.R
import net.squanchy.service.firebase.model.twitter.FirestoreTwitterHashtag
import net.squanchy.service.firebase.model.twitter.FirestoreTwitterMention
import net.squanchy.service.firebase.model.twitter.FirestoreTwitterUrl
import net.squanchy.support.text.parseHtml
import java.util.regex.Pattern

class TweetUrlSpanFactory(private val context: Context) {

    @SuppressWarnings("LongParameterList")
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
        spanFactory.createFor("https://twitter.com/search?q=$text")

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
        spanFactory.createFor("https://twitter.com/$screenName")

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

    companion object {

        private val HTML_ENTITY_PATTERN = Pattern.compile("&#?\\w+;")
    }
}
