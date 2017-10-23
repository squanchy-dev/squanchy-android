package net.squanchy.tweets.view

import android.content.Context
import android.content.res.Resources
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
import java.util.regex.Pattern

class TweetUrlSpanFactory(private val context: Context) {

    fun applySpansToTweet(
            text: String,
            startIndex: Int,
            hashtags: List<HashtagEntity>,
            mentions: List<MentionEntity>,
            urls: List<UrlEntity>
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

    private fun offsetStart(hashtag: HashtagEntity, startIndex: Int): HashtagEntity {
        return HashtagEntity(
                hashtag.text,
                hashtag.start - startIndex,
                hashtag.end - startIndex
        )
    }

    private fun HashtagEntity.createUrlSpanWith(spanFactory: TweetUrlSpanFactory): TweetUrlSpan =
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

    private fun offsetStart(mention: MentionEntity, startIndex: Int): MentionEntity {
        return MentionEntity(
                mention.id,
                mention.idStr,
                mention.name,
                mention.screenName,
                mention.start - startIndex,
                mention.end - startIndex
        )
    }

    private fun MentionEntity.createUrlSpanWith(spanFactory: TweetUrlSpanFactory): TweetUrlSpan =
        spanFactory.createFor(String.format(MENTION_URL_TEMPLATE, screenName))

    private fun offsetStart(url: UrlEntity, startIndex: Int): UrlEntity {
        return UrlEntity(
                url.url,
                url.expandedUrl,
                url.displayUrl,
                url.start - startIndex,
                url.end - startIndex
        )
    }

    private fun UrlEntity.createUrlSpanWith(spanFactory: TweetUrlSpanFactory): TweetUrlSpan =
        spanFactory.createFor(url)

    private fun unescapeEntities(builder: SpannableStringBuilder) {
        val string = builder.toString()
        val matcher = HTML_ENTITY_PATTERN.matcher(string)

        if (matcher.find()) {
            val matchResult = matcher.toMatchResult()
            val unescapedEntity = Html.fromHtml(matchResult.group())
            builder.replace(matchResult.start(), matchResult.end(), unescapedEntity)
            unescapeEntities(builder)
        }
    }

    companion object {
        private const val BASE_TWITTER_URL = "https://twitter.com/"
        private const val MENTION_URL_TEMPLATE = BASE_TWITTER_URL + "%s"
        private const val QUERY_URL_TEMPLATE = BASE_TWITTER_URL + "search?q=%s"

        private val HTML_ENTITY_PATTERN = Pattern.compile("&#?\\w+;")
    }
}
