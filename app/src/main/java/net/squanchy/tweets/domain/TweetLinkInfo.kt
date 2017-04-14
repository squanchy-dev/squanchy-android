package net.squanchy.tweets.domain

import com.twitter.sdk.android.core.models.Tweet

data class TweetLinkInfo(private val tweet: Tweet) {
    val statusId: String
        get() = tweet.idStr
    val screenName: String
        get() = tweet.user.screenName

    companion object {

        fun create(tweet: Tweet): TweetLinkInfo = TweetLinkInfo(tweet)
    }
}
