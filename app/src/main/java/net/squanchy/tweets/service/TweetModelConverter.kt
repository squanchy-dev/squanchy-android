package net.squanchy.tweets.service

import net.squanchy.service.firebase.model.twitter.FirestoreTweet
import net.squanchy.service.firebase.model.twitter.FirestoreTwitterHashtag
import net.squanchy.service.firebase.model.twitter.FirestoreTwitterMedia
import net.squanchy.service.firebase.model.twitter.FirestoreTwitterMention
import net.squanchy.service.firebase.model.twitter.FirestoreTwitterUrl
import net.squanchy.tweets.domain.TweetLinkInfo
import net.squanchy.tweets.domain.view.TweetViewModel
import net.squanchy.tweets.domain.view.User
import net.squanchy.tweets.view.TweetUrlSpanFactory

private const val MEDIA_TYPE_PHOTO = "photo"

fun mapToViewModel(factory: TweetUrlSpanFactory, tweet: FirestoreTweet): TweetViewModel {
    val user = User(name = tweet.user.name, screenName = tweet.user.screenName, photoUrl = tweet.user.profileImageUrl)

    val emojiIndices = findEmojiIndices(tweet.text)
    val displayTextRange = Range.from(tweet.displayTextRange, tweet.text.length, emojiIndices.size)

    val hashtags = adjustHashtag(onlyHashtagsInRange(tweet.entities.hashtags, displayTextRange), emojiIndices)
    val mentions = adjustMentions(onlyMentionsInRange(tweet.entities.userMentions, displayTextRange), emojiIndices)
    val urls = adjustUrls(onlyUrlsInRange(tweet.entities.urls, displayTextRange), emojiIndices)
    val photoUrls = onlyPhotoUrls(tweet.entities.media)
    val unresolvedPhotoUrl = tweet.entities.media.map { it.url }
    val displayableText = displayableTextFor(tweet.text, displayTextRange, unresolvedPhotoUrl)

    return TweetViewModel(
        id = tweet.id.toLong(),
        text = displayableText,
        spannedText = factory.applySpansToTweet(displayableText, displayTextRange.start(), hashtags, mentions, urls),
        user = user,
        createdAt = tweet.createdAt,
        photoUrl = photoUrlMaybeFrom(photoUrls),
        linkInfo = TweetLinkInfo(tweet)
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

private fun onlyHashtagsInRange(entities: List<FirestoreTwitterHashtag>, displayTextRange: Range): List<FirestoreTwitterHashtag> =
    entities.filter { displayTextRange.contains(it.start, it.end) }

private fun onlyMentionsInRange(entities: List<FirestoreTwitterMention>, displayTextRange: Range): List<FirestoreTwitterMention> =
    entities.filter { displayTextRange.contains(it.start, it.end) }

private fun onlyUrlsInRange(entities: List<FirestoreTwitterUrl>, displayTextRange: Range): List<FirestoreTwitterUrl> =
    entities.filter { displayTextRange.contains(it.start, it.end) }

private fun adjustHashtag(entities: List<FirestoreTwitterHashtag>, indices: List<Int>): List<FirestoreTwitterHashtag> {
    for (hashtag in entities) {
        val offset = offsetFrom(hashtag.start, indices)
        hashtag.start = hashtag.start + offset
        hashtag.end = hashtag.end + offset
    }
    return entities
}

private fun adjustMentions(entities: List<FirestoreTwitterMention>, indices: List<Int>): List<FirestoreTwitterMention> {
    for (mention in entities) {
        val offset = offsetFrom(mention.start, indices)
        mention.start = mention.start + offset
        mention.end = mention.end + offset
    }
    return entities
}

private fun adjustUrls(entities: List<FirestoreTwitterUrl>, indices: List<Int>): List<FirestoreTwitterUrl> {
    for (url in entities) {
        val offset = offsetFrom(url.start, indices)
        url.start = url.start + offset
        url.end = url.end + offset
    }
    return entities
}

private fun offsetFrom(start: Int, indices: List<Int>): Int {
    var offset = 0
    indices.takeWhile { it - offset <= start }
        .forEach { offset += 1 }

    return offset
}

private fun onlyPhotoUrls(media: List<FirestoreTwitterMedia>): List<String> {
    return media.filter { it.type == MEDIA_TYPE_PHOTO }
        .map { it.mediaUrl }
}

private fun displayableTextFor(text: String, displayTextRange: Range, photoUrls: List<String>): String {
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
