package net.squanchy.tweets.service

import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import com.twitter.sdk.android.core.models.HashtagEntity
import com.twitter.sdk.android.core.models.MentionEntity
import com.twitter.sdk.android.core.models.UrlEntity
import net.squanchy.tweets.view.TweetUrlSpan
import net.squanchy.tweets.view.TweetUrlSpanFactory
import java.util.regex.Pattern

class TweetSpannedTextBuilder(private val spanFactory: TweetUrlSpanFactory) {

    internal fun applySpans(
            text: String,
            startIndex: Int,
            hashtags: List<HashtagEntity>,
            mentions: List<MentionEntity>,
            urls: List<UrlEntity>
    ): Spanned {
        val builder = SpannableStringBuilder(text)

        hashtags.forEach {
            val hashtag = offsetStart(it, startIndex)
            builder.setSpan(createUrlSpanFor(hashtag), hashtag.start, hashtag.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        mentions.forEach {
            val mention = offsetStart(it, startIndex)
            builder.setSpan(createUrlSpanFor(mention), mention.start, mention.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        urls.forEach {
            val url = offsetStart(it, startIndex)
            builder.setSpan(createUrlSpanFor(url), url.start, url.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
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

    private fun createUrlSpanFor(hashtag: HashtagEntity): TweetUrlSpan =
        span(String.format(QUERY_URL_TEMPLATE, hashtag.text))

    private fun span(url: String): TweetUrlSpan = spanFactory.createFor(url)

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

    private fun createUrlSpanFor(mention: MentionEntity): TweetUrlSpan =
        span(String.format(MENTION_URL_TEMPLATE, mention.screenName))

    private fun offsetStart(url: UrlEntity, startIndex: Int): UrlEntity {
        return UrlEntity(
                url.url,
                url.expandedUrl,
                url.displayUrl,
                url.start - startIndex,
                url.end - startIndex
        )
    }

    private fun createUrlSpanFor(url: UrlEntity): TweetUrlSpan = span(url.url)

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

        @JvmStatic
        private val HTML_ENTITY_PATTERN = Pattern.compile("&#?\\w+;")
    }
}
