package net.squanchy.tweets.service

import com.twitter.sdk.android.core.models.HashtagEntity
import com.twitter.sdk.android.core.models.MediaEntity
import com.twitter.sdk.android.core.models.MentionEntity
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.UrlEntity
import net.squanchy.tweets.domain.TweetLinkInfo
import net.squanchy.tweets.domain.view.TweetViewModel
import net.squanchy.tweets.domain.view.User
import net.squanchy.tweets.view.TweetUrlSpanFactory

private const val MEDIA_TYPE_PHOTO = "photo"

fun mapToViewModel(factory: TweetUrlSpanFactory, tweet: Tweet): TweetViewModel {
    val user = User.create(tweet.user.name, tweet.user.screenName, tweet.user.profileImageUrlHttps)

    val emojiIndices = findEmojiIndices(tweet.text)
    val displayTextRange = Range.from(tweet.displayTextRange, tweet.text.length, emojiIndices.size)

    val hashtags = adjustHashtag(onlyHashtagsInRange(tweet.entities.hashtags, displayTextRange), emojiIndices)
    val mentions = adjustMentions(onlyMentionsInRange(tweet.entities.userMentions, displayTextRange), emojiIndices)
    val urls = adjustUrls(onlyUrlsInRange(tweet.entities.urls, displayTextRange), emojiIndices)
    val photoUrls = onlyPhotoUrls(tweet.entities.media)
    val unresolvedPhotoUrl = tweet.entities.media.map { it.url }
    val displayableText = displayableTextFor(tweet.text, displayTextRange, unresolvedPhotoUrl)

    return TweetViewModel.create(
            tweet.id,
            displayableText,
            factory.applySpansToTweet(displayableText, displayTextRange.start(), hashtags, mentions, urls),
            user,
            tweet.createdAt,
            photoUrlMaybeFrom(photoUrls),
            TweetLinkInfo.create(tweet)
    )
}

private fun findEmojiIndices(text: String): List<Int> {
    val emojiIndices = mutableListOf<Int>()
    text.forEachIndexed { index, char ->
        if (Character.isHighSurrogate(char) && Character.isLowSurrogate(text[index + 1])) {
            emojiIndices.add(index)
        }
    }
    return emojiIndices
}

private fun onlyHashtagsInRange(entities: List<HashtagEntity>, displayTextRange: Range): List<HashtagEntity> =
    entities.filter { displayTextRange.contains(it.start, it.end) }

private fun onlyMentionsInRange(entities: List<MentionEntity>, displayTextRange: Range): List<MentionEntity> =
    entities.filter { displayTextRange.contains(it.start, it.end) }

private fun onlyUrlsInRange(entities: List<UrlEntity>, displayTextRange: Range): List<UrlEntity> =
    entities.filter { displayTextRange.contains(it.start, it.end) }

private fun adjustHashtag(entities: List<HashtagEntity>, indices: List<Int>): List<HashtagEntity> {
    val hashtags = ArrayList<HashtagEntity>(entities.size)
    for (hashtag in entities) {
        val offset = offsetFrom(hashtag.start, indices)
        hashtags.add(HashtagEntity(hashtag.text, hashtag.start + offset, hashtag.end + offset))
    }
    return hashtags
}

private fun adjustMentions(entities: List<MentionEntity>, indices: List<Int>): List<MentionEntity> {
    val mentions = ArrayList<MentionEntity>(entities.size)
    for (mention in entities) {
        val offset = offsetFrom(mention.start, indices)
        mentions.add(
                MentionEntity(
                        mention.id,
                        mention.idStr,
                        mention.name,
                        mention.screenName,
                        mention.start + offset,
                        mention.end + offset
                )
        )
    }
    return mentions
}

private fun adjustUrls(entities: List<UrlEntity>, indices: List<Int>): List<UrlEntity> {
    val urls = ArrayList<UrlEntity>(entities.size)
    for (url in entities) {
        val offset = offsetFrom(url.start, indices)
        urls.add(UrlEntity(url.url, url.expandedUrl, url.displayUrl, url.start + offset, url.end + offset))
    }
    return urls
}

private fun offsetFrom(start: Int, indices: List<Int>): Int {
    var offset = 0
    indices.takeWhile { it - offset <= start }
        .forEach { offset += 1 }

    return offset
}

private fun onlyPhotoUrls(media: List<MediaEntity>): List<String> {
    return media.filter { it.type == MEDIA_TYPE_PHOTO }
        .map { it.mediaUrlHttps }
}

private fun displayableTextFor(
    text: String,
    displayTextRange: Range,
    photoUrls: List<String>
): String {
    val beginIndex = displayTextRange.start()
    val endIndex = displayTextRange.end()
    val displayableText = text.substring(beginIndex, endIndex)
    return removeLastPhotoUrl(displayableText, photoUrls)
}

private fun removeLastPhotoUrl(content: String, photoUrls: List<String>): String {
    if (photoUrls.isEmpty()) {
        return content
    }
    val lastUrl = photoUrls[photoUrls.size - 1]
    return if (content.endsWith(lastUrl)) {
        content.replace(lastUrl, "")
            .trim { it <= ' ' }
    } else {
        content
    }
}

private fun photoUrlMaybeFrom(urls: List<String>): String? =
    if (urls.isEmpty()) null else urls[0]

private class Range(private val start: Int, private val end: Int) {

    internal fun start() = start

    internal fun end() = end

    internal fun contains(start: Int, end: Int): Boolean = start() <= start && end <= end()

    companion object {

        internal fun from(positions: List<Int>, textLength: Int, offset: Int): Range {
            return if (positions.size != 2) {
                Range(0, textLength - 1)
            } else {
                Range(positions[0], positions[1] + offset)
            }
        }
    }
}
