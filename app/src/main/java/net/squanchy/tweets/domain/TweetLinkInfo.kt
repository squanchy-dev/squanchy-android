package net.squanchy.tweets.domain

import net.squanchy.service.firebase.model.twitter.FirestoreTweet

data class TweetLinkInfo(private val tweet: FirestoreTweet) {
    val statusId: String
        get() = tweet.id
    val screenName: String
        get() = tweet.user.screenName
}
